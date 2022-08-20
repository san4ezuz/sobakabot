package com.apuzanov.sobakabot.componets

import com.apuzanov.sobakabot.handlers.base.CommandHandler
import com.apuzanov.sobakabot.handlers.base.UpdateHandler
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.update.abstracts.UnknownUpdate
import dev.inmo.tgbotapi.types.update.abstracts.Update
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class UpdatesManager(
    handlers: List<UpdateHandler>
) {
    companion object {
        private val log = LogManager.getLogger()
    }

    private val handlers = handlers.sortedBy { it.order }

    fun getAllowedUpdates(): List<String> {
        return handlers.map { it.updateType }.distinct()
    }

    fun getCommands(): List<BotCommand> {
        return handlers.filterIsInstance(CommandHandler::class.java)
            .map { it.getCommandInfo() }
    }

    suspend fun processUpdate(update: Update) {

        if (update is UnknownUpdate) {
            log.error("Unknown update type: $update")
            return
        }

        for (handler in handlers) {
            try {
                val answered = handler.handleUpdate(update)
                if (answered) {
                    break
                }
            } catch (e: Exception) {
                log.error("Handler: ${handler::class.simpleName}, update: $update", e)
            }
        }
    }
}
