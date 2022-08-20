package com.apuzanov.sobakabot.repository

import com.apuzanov.sobakabot.entity.HandledMediaCache
import org.springframework.data.jpa.repository.JpaRepository

interface HandledMediaCacheRepository : JpaRepository<HandledMediaCache, Int> {
    fun findByFileIdAndChatId(fileId: String, chatId: String): HandledMediaCache?
}
