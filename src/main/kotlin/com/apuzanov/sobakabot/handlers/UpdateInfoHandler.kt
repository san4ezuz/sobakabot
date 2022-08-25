package com.apuzanov.sobakabot.handlers

import com.apuzanov.sobakabot.handlers.base.MessageHandler
import com.apuzanov.sobakabot.service.ChatService
import com.apuzanov.sobakabot.service.UserService
import com.apuzanov.sobakabot.utils.usernameOrName
import dev.inmo.tgbotapi.types.chat.PublicChat
import dev.inmo.tgbotapi.types.message.abstracts.FromUserMessage
import dev.inmo.tgbotapi.types.message.abstracts.Message
import org.springframework.stereotype.Component

@Component
class UpdateInfoHandler(
    private val userService: UserService,
    private val chatService: ChatService
) : MessageHandler() {
    override val order = 0

    override suspend fun handleMessage(message: Message): Boolean {
        if (message is FromUserMessage) {
            userService.getUser(message.user.id)?.let {
                val actualUsername = message.user.usernameOrName

                if (it.username != actualUsername) {
                    userService.updateUsername(it, actualUsername)
                }
            }
        }
        val chat = message.chat
        if (chat is PublicChat) {
            chatService.getChat(chat.id)?.let {
                val actualTitle = chat.title

                if (it.title != actualTitle) {
                    chatService.updateTitle(it, actualTitle)
                }
            }
        }
        return false
    }
}
