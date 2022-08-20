package com.apuzanov.sobakabot.clients

import com.apuzanov.sobakabot.config.VKCloudApiProperties
import com.fasterxml.jackson.annotation.JsonAlias
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.springframework.stereotype.Component

@Component
class VoiceClient(
    private val httpClient: HttpClient,
    private val vkCloudApiProperties: VKCloudApiProperties
) {

    private data class ASRResponse(val qid: String, val result: ASRResult)
    private data class ASRResult(val texts: Array<ASRTextResult>)
    private data class ASRTextResult(val text: String, val confidence: Double, @JsonAlias("punctuated_text") val punctuatedText: String)

    suspend fun getTextFromSpeech(audioBytes: ByteArray): String {
        val resp = httpClient.post {
            url("https://voice.mcs.mail.ru/asr")
            headers {
                append(HttpHeaders.Authorization, "Bearer ${vkCloudApiProperties.key}")
            }
            setBody(ByteArrayContent(audioBytes, ContentType.parse("audio/ogg; codecs=opus")))
            timeout {
                socketTimeoutMillis = 10 * 60 * 1000
            }
        }.body<ASRResponse>()
        val text = resp.result.texts.maxByOrNull { it.confidence }
        return text?.punctuatedText ?: ""
    }
}
