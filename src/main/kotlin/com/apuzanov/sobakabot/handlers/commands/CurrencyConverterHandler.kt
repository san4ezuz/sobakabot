package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.handlers.base.spacesRegex
import com.apuzanov.sobakabot.service.CurrencyService
import com.apuzanov.sobakabot.service.UnknownCurrencyException
import com.apuzanov.sobakabot.utils.calculateIncreasePercentage
import com.apuzanov.sobakabot.utils.roundToSignificantDigitsAfterComma
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Component
class CurrencyConverterHandler(
    private val currencyService: CurrencyService,
    private val requestsExecutor: RequestsExecutor,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("currency", "cur"),
    commandDescription = "Перевод валют"
) {
    companion object {
        private val WRONG_MESSAGES = "Пришли мне в таком виде: /currency <amount> <from> <to>"

        private val mc = MathContext(2, RoundingMode.HALF_UP)
        private val df = DecimalFormat.getInstance(Locale.US).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = Integer.MAX_VALUE
        }
        private val diffFormat = (DecimalFormat.getInstance(Locale.US) as DecimalFormat).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = Integer.MAX_VALUE
            positivePrefix = "+"
        }
    }

    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        if (args === null) {
            requestsExecutor.reply(message, WRONG_MESSAGES)
            return
        }
        val regex = """^(?<amount>\d+?\.?\d*)\s+(?<from>[A-z]{3})\s+(?<to>[A-z]{3})$""".toRegex()
        val currencyMessage = regex.find(args)
        if (currencyMessage == null) {
            requestsExecutor.reply(message, WRONG_MESSAGES)
            return
        }
        val (amount, from, to) = currencyMessage.value.split(spacesRegex, limit = 3)
        val originalAmount = amount.toBigDecimal()
        val (convertedAmount, convertedAmountDayAgo) = try {
            currencyService.convertCurrency(originalAmount, from, to)
        } catch (e: UnknownCurrencyException) {
            requestsExecutor.reply(message, "Не знаю про ${e.currency}")
            return
        }

        val amountDiff = convertedAmount - convertedAmountDayAgo
        val percentDiff = calculateIncreasePercentage(convertedAmountDayAgo, convertedAmount)

        val diffText = "${formatDiff(amountDiff, to)} / ${formatDiff(percentDiff, "%")}"

        val text = """
            Твои жалкие ${formatAmount(originalAmount, from)} сейчас равны ${formatAmount(convertedAmount, to)}
            Вчера - ${formatAmount(convertedAmountDayAgo, to)} ($diffText)
            """.trimIndent()

        requestsExecutor.reply(message, text)
    }

    private fun formatAmount(originalAmount: BigDecimal, currency: String) =
        "${df.format(originalAmount.roundToSignificantDigitsAfterComma(mc))} ${currency.uppercase()}"

    private fun formatDiff(diff: BigDecimal, currency: String) =
        "${diffFormat.format(diff.roundToSignificantDigitsAfterComma(mc))} ${currency.uppercase()}"
}
