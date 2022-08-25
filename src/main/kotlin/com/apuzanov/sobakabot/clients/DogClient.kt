package com.apuzanov.sobakabot.clients

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component

@Component
class DogClient(
    private val httpClient: HttpClient
) {
    suspend fun getDog(): HttpResponse {
        val dog: Dog = httpClient.get("https://random.dog/woof.json").body()
        return httpClient.get(dog.url)
    }
}

data class Dog(val fileSizeBytes: String, val url: String)