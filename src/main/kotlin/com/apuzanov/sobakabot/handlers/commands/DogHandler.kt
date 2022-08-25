package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.clients.DogClient
import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.utils.asMultipartFile
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.replyWithPhoto
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component


@Component
class DogHandler(
    private val requestsExecutor: RequestsExecutor,
    private val dogClient: DogClient,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("dog"),
    commandDescription = "сгенерировать песика"
) {
    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val dogResponse = dogClient.getDog()

        requestsExecutor.replyWithPhoto(
            message,

            dogResponse.asMultipartFile("dog")
        )
    }
}