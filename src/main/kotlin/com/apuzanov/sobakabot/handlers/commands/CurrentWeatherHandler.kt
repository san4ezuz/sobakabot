package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.service.WeatherService
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.utils.asContentMessage
import dev.inmo.tgbotapi.extensions.utils.asLocationContent
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component

@Component
class CurrentWeatherHandler(
    private val weatherService: WeatherService,
    private val requestsExecutor: RequestsExecutor,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("weather"),
    commandDescription = "Текущая погода"
) {

    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val locationContent = message.replyTo?.asContentMessage()?.content?.asLocationContent()

        var defaultLocationChosen = false

        val weatherForLocation = if (locationContent !== null) {
            val location = locationContent.location
            weatherService.getWeatherForLocation(location.latitude, location.longitude)
        } else if (!args.isNullOrBlank()) {
            weatherService.getWeatherForLocation(args)
        } else {
            defaultLocationChosen = true
            weatherService.getWeatherForLocation("Санкт-Петербург")
        }

        if (weatherForLocation === null) {
            requestsExecutor.reply(message, "Не знаю такого")
            return
        }

        val formattedMessage =
            "Сейчас " + weatherForLocation.main.temp + "℃ " + weatherForLocation.weather[0].description

        requestsExecutor.reply(
            message,
            if (defaultLocationChosen) {
                "Ты не уточнил, поэтому вот результат для дефолт-сити: $formattedMessage"
            } else {
                formattedMessage
            }
        )
    }
}