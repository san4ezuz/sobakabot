package com.apuzanov.sobakabot.handlers

import com.apuzanov.sobakabot.handlers.base.CommonMessageHandler
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.utils.asPossiblySentViaBotCommonMessage
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from

import dev.inmo.tgbotapi.extensions.utils.extensions.raw.via_bot
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class RoyzmanHandler(
    private val requestsExecutor: RequestsExecutor
) : CommonMessageHandler() {
    companion object {
        private val log = LogManager.getLogger()
    }

    override suspend fun handleMessage(message: CommonMessage<*>): Boolean {
        val user = message.via_bot?: return  false
        log.info("user:" + user.id +" username: "+user.username +" firstname: " + (user.lastName))
        return true
    }
}