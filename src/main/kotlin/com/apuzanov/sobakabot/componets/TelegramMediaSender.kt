package com.apuzanov.sobakabot.componets

import com.apuzanov.sobakabot.entity.MediaCache
import com.apuzanov.sobakabot.repository.MediaCacheRepository
import com.apuzanov.sobakabot.service.DigestService
import com.apuzanov.sobakabot.utils.asMultipartFile
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.media.sendAnimation
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.requests.abstracts.InputFile
import dev.inmo.tgbotapi.requests.abstracts.toInputFile
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.MessageIdentifier
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.MediaContent
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class TelegramMediaSender(
    private val requestsExecutor: RequestsExecutor,
    private val digestService: DigestService,
    private val mediaCacheRepository: MediaCacheRepository
) {
    suspend fun sendPhoto(
        chatId: ChatIdentifier,
        resource: ClassPathResource,
        replyTo: MessageIdentifier?
    ) {
        sendMedia(resource) { file ->
            requestsExecutor.sendPhoto(chatId, file, replyToMessageId = replyTo)
        }
    }

    suspend fun sendPhotoWithText(
        chatId: ChatIdentifier,
        resource: ClassPathResource,
        text: String,
        replyTo: MessageIdentifier?
    ) {
        sendMedia(resource) { file ->
            requestsExecutor.sendPhoto(chatId, file, text, replyToMessageId = replyTo)
        }
    }

    suspend fun sendAnimation(
        chatId: ChatIdentifier,
        resource: ClassPathResource,
        replyTo: MessageIdentifier?
    ) {
        sendMedia(resource) { file ->
            requestsExecutor.sendAnimation(chatId, file, replyToMessageId = replyTo)
        }
    }

    suspend fun <T : MediaContent> sendMedia(
        resource: ClassPathResource,
        fileSender: suspend (file: InputFile) -> ContentMessage<T>,
    ) {
        val digest = getResourceDigest(resource)

        val cache = mediaCacheRepository.findByDigest(digest)
        if (cache != null) {
            try {
                fileSender(cache.fileId.toInputFile())
            } catch (e: Exception) {
                val message = fileSender(resource.asMultipartFile())
                saveMediaCache(digest, message.content)
            }
        } else {
            val message = fileSender(resource.asMultipartFile())
            saveMediaCache(digest, message.content)
        }
    }

    private fun getResourceDigest(resource: ClassPathResource): String {
        return resource.inputStream.use { digestService.getInputStreamDigest(it) }
    }

    private fun saveMediaCache(digest: String, mediaContent: MediaContent) {
        mediaCacheRepository.save(MediaCache(digest, mediaContent.media.fileId.fileId))
    }
}
