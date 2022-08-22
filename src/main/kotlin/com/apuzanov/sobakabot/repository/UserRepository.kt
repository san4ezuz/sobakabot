package com.apuzanov.sobakabot.repository

import com.apuzanov.sobakabot.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByTelegramId(telegramId: Long): User?
}
