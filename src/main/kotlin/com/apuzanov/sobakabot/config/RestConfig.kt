package com.apuzanov.sobakabot.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestConfig {
    @Bean
    fun httpClient(objectMapper: ObjectMapper): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                register(ContentType.Any, JacksonConverter(objectMapper))
            }

            install(HttpTimeout)
        }
    }
}
