package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.clients.FoxClient
import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.utils.asMultipartFile
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.replyWithPhoto
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component

@Component
class FoxHandler(
    private val requestsExecutor: RequestsExecutor,
    private val foxClient: FoxClient,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("fox"),
    commandDescription = "сгенерировать лисичку"
) {
    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val foxResponse = foxClient.getFox()

        requestsExecutor.replyWithPhoto(
            message,

            foxResponse.asMultipartFile("fox")
        )
    }
}