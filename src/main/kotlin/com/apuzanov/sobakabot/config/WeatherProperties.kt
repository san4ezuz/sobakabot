package com.apuzanov.sobakabot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "weather.api")
@ConstructorBinding
@Validated
data class WeatherProperties(
    @field:NotBlank
    val key: String
)