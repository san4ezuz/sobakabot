package com.apuzanov.sobakabot.clients

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class FoxClient(
    private val httpClient: HttpClient
) {
    suspend fun getFox(): HttpResponse {
        val id = Random().nextInt(121)
        return httpClient.get(" https://randomfox.ca/images/$id.jpg")
    }
}