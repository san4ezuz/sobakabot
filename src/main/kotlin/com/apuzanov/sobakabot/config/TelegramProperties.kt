package com.apuzanov.sobakabot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@ConfigurationProperties(prefix = "telegram.bot")
@ConstructorBinding
@Validated
data class TelegramProperties(
    val token: String,
    val webhook: WebhookProperties = WebhookProperties()
)

data class WebhookProperties(
    val url: String? = null,
    @field:Min(0)
    @field:Max(65535)
    val port: Int = 8080
)
