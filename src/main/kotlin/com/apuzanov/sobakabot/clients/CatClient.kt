package com.apuzanov.sobakabot.clients

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component

@Component
class CatClient(
    private val httpClient: HttpClient
) {
    suspend fun getCat(): HttpResponse {
        return httpClient.get("https://thiscatdoesnotexist.com/")
    }
}