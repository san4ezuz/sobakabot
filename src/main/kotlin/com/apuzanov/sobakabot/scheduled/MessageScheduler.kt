package com.apuzanov.sobakabot.scheduled

import com.apuzanov.sobakabot.componets.TelegramMediaSender
import com.apuzanov.sobakabot.service.CurrencyService
import com.apuzanov.sobakabot.service.WeatherService
import com.apuzanov.sobakabot.utils.roundToSignificantDigitsAfterComma
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Component
@EnableScheduling
class MessageScheduler(
    private val telegramMediaSender: TelegramMediaSender,
    private val requestsExecutor: RequestsExecutor,
    private val weatherService: WeatherService,
    private val currencyService: CurrencyService
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
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    @Scheduled(cron = "0 0 9 ? * TUE", zone = "Europe/Moscow")
    fun morningMessageWithMemeTue() {
        runBlocking {
            val meme = getMemeByDay("tuesday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    @Scheduled(cron = "0 0 9 ? * WED", zone = "Europe/Moscow")
    fun morningMessageWithMemeWed() {
        runBlocking {
            val meme = getMemeByDay("wednesday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }

    }

    @Scheduled(cron = "0 0 9 ? * THU", zone = "Europe/Moscow")
    fun morningMessageWithMemeThu() {
        runBlocking {
            val meme = getMemeByDay("thursday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    @Scheduled(cron = "0 0 9 ? * FRI", zone = "Europe/Moscow")
    fun morningMessageWithMemeFri() {
        runBlocking {
            val meme = getMemeByDay("friday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    @Scheduled(cron = "0 0 9 ? * SAT", zone = "Europe/Moscow")
    fun morningMessageWithMemeSat() {
        runBlocking {
            val meme = getMemeByDay("saturday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    @Scheduled(cron = "0 0 9 ? * SUN", zone = "Europe/Moscow")
    fun morningMessageWithMemeSun() {
        runBlocking {
            val meme = getMemeByDay("sunday")
            telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
        }
    }

    private fun getMemeByDay(day: String): ClassPathResource {
        var rand = Random().nextInt(20)
        if (day.equals("wednesday")) {
            rand = Random().nextInt(26)
        }
        return ClassPathResource("memes/$day/$rand.png")
    }

    @Scheduled(cron = "0 0 7 * * ?")
    fun morningMessage() {
        runBlocking {

            val (convertedAmountUSD, convertedAmountDayAgoUSD) = currencyService.convertCurrency(
                BigDecimal.ONE,
                "USD",
                "RUB"
            )
            val (convertedAmountBTC, convertedAmountDayAgoBTC) = currencyService.convertCurrency(
                BigDecimal.ONE,
                "BTC",
                "USD"
            )
            val weather = weatherService.getWeatherForLocation("Санкт-Петербург")
            if (weather != null) {
                val message = "Доброе утро! Желаю чудесного дня!\n" +
                        "Сейчас в Санкт-Петербурге " + weather.main.temp + "℃ " + weather.weather[0].description + "\n" +
                        "Курс валют на сегодня: 1 USD = ${formatAmount(convertedAmountUSD, "RUB")}," +
                        " 1 BTC = ${formatAmount(convertedAmountBTC, "USD")}"
                requestsExecutor.sendTextMessage(ChatId(CHAT_ID), message)
                val meme = getMemeByDay("wednesday")
                telegramMediaSender.sendPhoto(ChatId(CHAT_ID), meme)
            }

        }
    }

    private val df = DecimalFormat.getInstance(Locale.US).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = Integer.MAX_VALUE
    }

    private val mc = MathContext(2, RoundingMode.HALF_UP)

    private fun formatAmount(originalAmount: BigDecimal, currency: String) =
        "${df.format(originalAmount.roundToSignificantDigitsAfterComma(mc))} ${currency.uppercase()}"
}