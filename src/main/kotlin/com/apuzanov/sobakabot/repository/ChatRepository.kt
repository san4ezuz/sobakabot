package com.apuzanov.sobakabot.repository

import com.apuzanov.sobakabot.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Short> {
    fun findByTelegramId(telegramId: Long): Chat?
}
