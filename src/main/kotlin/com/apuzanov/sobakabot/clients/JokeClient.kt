package com.apuzanov.sobakabot.clients

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component

@Component
class JokeClient(
    private val httpClient: HttpClient
) {
    suspend fun getJoke(): HttpResponse {
        return httpClient.get("http://rzhunemogu.ru/RandJSON.aspx?CType=1")
    }
}