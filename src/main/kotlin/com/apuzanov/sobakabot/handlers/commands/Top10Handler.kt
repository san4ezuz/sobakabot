package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.service.ChatService
import com.apuzanov.sobakabot.service.UserService
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.utils.asPublicChat
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component

@Component
class Top10Handler(
    botInfo: ExtendedBot,
    private val chatService: ChatService,
    private val userService: UserService,
    private val requestsExecutor: RequestsExecutor
) : CommandHandler(
    botInfo,
    command = arrayOf("top10"),
    commandDescription = "кто больше всех пишет"
) {
    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val chat = message.chat.asPublicChat() ?: return

        val (chatEntity, _) = chatService.getOrCreateChatFrom(chat)

        val top = userService.getTop(chatEntity, limit = 10)

        if (top.isEmpty()) {
            return
        }

        val text = top
            .mapIndexed { index, statistic -> "${(index + 1)}. ${statistic.user.username} - ${statistic.messagesCount}" }
            .joinToString("\n")

        requestsExecutor.reply(message, text, disableNotification = true)
    }
}
