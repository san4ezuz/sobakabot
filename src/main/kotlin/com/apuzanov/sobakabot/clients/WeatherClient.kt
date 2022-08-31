package com.apuzanov.sobakabot.clients

import com.apuzanov.sobakabot.config.WeatherProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.springframework.stereotype.Component

@Component
class WeatherClient(
    private val httpClient: HttpClient,
    private val weatherProperties: WeatherProperties
) {

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {

        return httpClient.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("appid", weatherProperties.key)
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("units", "metric")
            parameter("lang", "ru")
        }.body<WeatherResponse>()
    }

    data class WeatherResponse(val main: Main, val weather : List<WeatherItem>)

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class WeatherItem(val description: String)

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Main(val temp: String)
}