package com.apuzanov.sobakabot.service

import com.apuzanov.sobakabot.clients.LocationiqClient
import com.apuzanov.sobakabot.clients.WeatherClient
import io.ktor.client.plugins.*
import org.springframework.stereotype.Component

@Component
class WeatherService(
    private val locationiqClient: LocationiqClient,
    private val weatherClient: WeatherClient
) {
    suspend fun getWeatherForLocation(locationQuery: String): WeatherClient.WeatherResponse? {
        val locations = try {
            locationiqClient.getLocation(locationQuery)
        } catch (e: ClientRequestException) {
            return null
        }

        val location = locations.first()

        return weatherClient.getWeather(location.lat, location.lon)
    }

    suspend fun getWeatherForLocation(latitude: Double, longitude: Double): WeatherClient.WeatherResponse? {
        return weatherClient.getWeather(latitude, longitude)
    }
}