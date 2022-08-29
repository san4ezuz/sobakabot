package com.apuzanov.sobakabot.scheduled

import com.apuzanov.sobakabot.componets.TelegramMediaSender
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableScheduling
class MessageScheduler(
    private val requestsExecutor: RequestsExecutor,
    private val telegramMediaSender: TelegramMediaSender
) {

    companion object {
        private val log = LogManager.getLogger()
        private const val CHAT_ID: Long = -1001584023709

    }

    //<minute> <hour> <day-of-month> <month> <day-of-week> <command>
    @Scheduled(cron = "0 0 9 ? * MON", zone = "Europe/Moscow")
    fun morningMessageWithMemeMon() {
        runBlocking {
            val meme = getMemeByDay("monday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    @Scheduled(cron = "0 0 9 ? * TUE", zone = "Europe/Moscow")
    fun morningMessageWithMemeTue() {
        runBlocking {
            val meme = getMemeByDay("tuesday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    @Scheduled(cron = "0 0 9 ? * WED", zone = "Europe/Moscow")
    fun morningMessageWithMemeWed() {
        runBlocking {
            val meme = getMemeByDay("wednesday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "IT'S WEEEEDNSDAY MY DUUUDDEES!")
            }
        }

    }

    @Scheduled(cron = "0 0 9 ? * THU", zone = "Europe/Moscow")
    fun morningMessageWithMemeThu() {
        runBlocking {
            val meme = getMemeByDay("thursday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    @Scheduled(cron = "0 0 9 ? * FRI", zone = "Europe/Moscow")
    fun morningMessageWithMemeFri() {
        runBlocking {
            val meme = getMemeByDay("friday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    @Scheduled(cron = "0 0 9 ? * SAT", zone = "Europe/Moscow")
    fun morningMessageWithMemeSat() {
        runBlocking {
            val meme = getMemeByDay("saturday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    @Scheduled(cron = "0 0 9 ? * SUN", zone = "Europe/Moscow")
    fun morningMessageWithMemeSun() {
        runBlocking {
            val meme = getMemeByDay("sunday")
            telegramMediaSender.sendMedia(meme) { file ->
                requestsExecutor.sendPhoto(ChatId(CHAT_ID), file, "Доброе утро, RESPECT! Всем хорошего дня!")
            }
        }
    }

    private fun getMemeByDay(day: String): ClassPathResource {
        var rand = Random().nextInt(20)
        if (day.equals("wednesday")) {
            rand = Random().nextInt(26)
        }
        return ClassPathResource("memes/$day/$rand.png")
    }
}