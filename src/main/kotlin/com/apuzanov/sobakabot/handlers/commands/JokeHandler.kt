package com.apuzanov.sobakabot.handlers.commands

import com.apuzanov.sobakabot.clients.JokeClient
import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.model.Joke
import com.apuzanov.sobakabot.utils.asMultipartFile
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.replyWithPhoto
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import io.ktor.client.call.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Component

@Component
class JokeHandler(
    private val requestsExecutor: RequestsExecutor,
    private val jokeClient: JokeClient,
    botInfo: ExtendedBot
) : CommandHandler(
    botInfo,
    command = arrayOf("joke"),
    commandDescription = "рассказать анекдот"
) {
    override suspend fun handleCommand(
        message: CommonMessage<TextContent>,
        args: String?
    ) {
        val jokeResponse = jokeClient.getJoke()
        println(jokeResponse.bodyAsText())
        val joke: Joke = jokeResponse.body()
        println(joke)

        requestsExecutor.reply(
            message, joke.content
        )
    }
}
