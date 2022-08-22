package com.apuzanov.sobakabot.handlers

import com.apuzanov.sobakabot.componets.TelegramMediaSender
import com.apuzanov.sobakabot.entity.HandledMediaCache
import com.apuzanov.sobakabot.handlers.base.CommonMessageHandler
import com.apuzanov.sobakabot.repository.HandledMediaCacheRepository
import dev.inmo.micro_utils.common.MPPFile
import dev.inmo.tgbotapi.extensions.utils.asFromUser
import dev.inmo.tgbotapi.extensions.utils.asMediaContent
import dev.inmo.tgbotapi.extensions.utils.asPhotoContent
import dev.inmo.tgbotapi.types.media.toTelegramMediaPhoto
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import org.apache.logging.log4j.LogManager
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class PhotoHandler(
    private val handledMediaCacheRepository: HandledMediaCacheRepository,
    private val telegramMediaSender: TelegramMediaSender
) : CommonMessageHandler() {

    companion object {
        private val log = LogManager.getLogger()
        private val sobakImage = ClassPathResource("media/sobak.png")
    }

    override val order = 0

    override suspend fun handleMessage(message: CommonMessage<*>): Boolean {
        val photo = message.content.asPhotoContent() ?: return false
        val user = message.asFromUser() ?: return false
        val chatId = message.chat.id
        val photoId = photo.media.fileUniqueId

        val cache = handledMediaCacheRepository.findByFileIdAndChatId(photoId, chatId.toString());
        if (cache != null) {
            val text = "Я собак, сожаю картошку!\n" +
                    "Первый раз эту картошку посадил: " +
                    user.user.firstName + " " + user.user.lastName + " " + cache.date.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            telegramMediaSender.sendPhotoWithText(
                chatId, sobakImage, text, replyTo = message.messageId
            )


        } else {
            val addedDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"))
            handledMediaCacheRepository.save(
                HandledMediaCache(
                    chatId.toString(),
                    user.user.id.toString(),
                    photoId,
                    addedDateTime
                )
            )
        }
        return true
    }
}
