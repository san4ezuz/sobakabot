package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.clients.DogClient
import com.apuzanov.sobakabot.clients.WaifuClient
import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.utils.asMultipartFile
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.replyWithPhoto
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component

@Component
class WaifuHandler(
    private val requestsExecutor: RequestsExecutor,
    private val waifuClient: WaifuClient,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("waifu"),
    commandDescription = "сгенерировать вайфу"
) {
    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val waifuResponse = waifuClient.getWaifu()

        requestsExecutor.replyWithPhoto(
            message,

            waifuResponse.asMultipartFile("waifu")
        )
    }
}