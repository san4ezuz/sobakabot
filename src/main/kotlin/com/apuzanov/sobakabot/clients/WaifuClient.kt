package com.apuzanov.sobakabot.clients

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component

@Component
class WaifuClient(
    private val httpClient: HttpClient
) {
    suspend fun getWaifu(): HttpResponse {
        val waifu: Waifu = httpClient.get("https://api.waifu.pics/sfw/waifu").body()
        return httpClient.get(waifu.url)
    }
}

data class Waifu(val url: String)