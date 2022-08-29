package com.apuzanov.sobakabot.handlers

import com.apuzanov.sobakabot.componets.TelegramMediaSender
import com.apuzanov.sobakabot.handlers.base.MessageHandler
import dev.inmo.tgbotapi.extensions.utils.asContentMessage
import dev.inmo.tgbotapi.extensions.utils.asPollContent
import dev.inmo.tgbotapi.types.message.abstracts.Message
import org.apache.logging.log4j.LogManager
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class PollHandler(
    private val telegramMediaSender: TelegramMediaSender
) : MessageHandler() {

    companion object {
        private val log = LogManager.getLogger()
        private val pollAudio = ClassPathResource("media/golosovanie.mp3")
    }

    override suspend fun handleMessage(message: Message): Boolean {
        val poll = message.asContentMessage()?.content?.asPollContent() ?: return false
        log.info("Poll " + poll.poll.question)
        telegramMediaSender.sendAudio(
            message.chat.id, pollAudio, replyTo = message.messageId
        )
        return true
    }
}