package com.apuzanov.sobakabot

import com.apuzanov.sobakabot.config.CacheProperties
import com.apuzanov.sobakabot.config.FixerApiProperties
import com.apuzanov.sobakabot.config.TelegramProperties
import com.apuzanov.sobakabot.config.VKCloudApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties(
    TelegramProperties::class,
    VKCloudApiProperties::class,
    FixerApiProperties::class,
    CacheProperties::class
)
class SobakabotApplication

fun main(args: Array<String>) {
    runApplication<SobakabotApplication>(*args)
}
