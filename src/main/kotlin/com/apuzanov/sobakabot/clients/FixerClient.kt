package com.apuzanov.sobakabot.clients

import com.apuzanov.sobakabot.config.FixerApiProperties
import com.apuzanov.sobakabot.utils.cached
import com.apuzanov.sobakabot.utils.getCacheOrThrow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class FixerClient(
    private val httpClient: HttpClient,
    private val fixerApiProperties: FixerApiProperties,
    private val cacheManager: CacheManager
) {
    suspend fun getLatestRates(): Map<String, BigDecimal> {
        val cache = cacheManager.getCacheOrThrow("fixerLatestRates")

        return cached(cache, "rates") {
            httpClient.get("https://api.apilayer.com/fixer/latest") {
                parameter("apikey", fixerApiProperties.key)
            }.body<LatestRates>().rates
        }
    }

    suspend fun getHistoricalRates(date: LocalDate): Map<String, BigDecimal> {
        val cache = cacheManager.getCacheOrThrow("fixerHistoricalRates")

        return cached(cache, date) {
            httpClient.get("https://api.apilayer.com/fixer/$date") {
                parameter("apikey", fixerApiProperties.key)
            }.body<HistoricalRates>().rates
        }
    }
}

data class LatestRates(val rates: Map<String, BigDecimal>)
data class HistoricalRates(val rates: Map<String, BigDecimal>)
