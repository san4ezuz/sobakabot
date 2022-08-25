package com.apuzanov.sobakabot.clients

import com.apuzanov.sobakabot.config.LocationiqApiProperties
import com.apuzanov.sobakabot.utils.cached
import com.apuzanov.sobakabot.utils.getCacheOrThrow
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.resilience4j.kotlin.ratelimiter.RateLimiterConfig
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.io.Serializable
import java.time.Duration

@Component
class LocationiqClient(
    private val httpClient: HttpClient,
    private val locationiqApiProperties: LocationiqApiProperties,
    private val cacheManager: CacheManager
) {
    companion object {
        private const val baseUrl = "https://eu1.locationiq.com"
        private val rateLimiter = RateLimiter.of("Locationiq", RateLimiterConfig {
            limitForPeriod(2)
            limitRefreshPeriod(Duration.ofSeconds(1))
            timeoutDuration(Duration.ofSeconds(1))
        })
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Location(val displayName: String, val lat: Double, val lon: Double) : Serializable

    suspend fun getLocation(query: String): List<Location> {
        val cache = cacheManager.getCacheOrThrow("locations")

        return cached(cache, query) {
            rateLimiter.executeSuspendFunction {
                httpClient.get("$baseUrl/v1/search.php") {
                    parameter("key", locationiqApiProperties.key)
                    parameter("format", "json")
                    parameter("accept-language", "ru")
                    parameter("q", query)
                }.body()
            }
        }
    }

    private data class TimezoneResponse(val timezone: Timezone)

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Timezone(val name: String, val shortName: String, val offsetSec: Long) : Serializable

    suspend fun getTimezone(latitude: Double, longitude: Double): Timezone {
        val cache = cacheManager.getCacheOrThrow("locationTimezones")

        return cached(cache, listOf(latitude, longitude)) {
            rateLimiter.executeSuspendFunction {
                httpClient.get("$baseUrl/v1/timezone.php") {
                    parameter("key", locationiqApiProperties.key)
                    parameter("lat", latitude)
                    parameter("lon", longitude)
                }.body<TimezoneResponse>().timezone
            }
        }
    }
}
