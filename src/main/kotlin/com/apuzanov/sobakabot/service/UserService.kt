package com.apuzanov.sobakabot.service

import com.apuzanov.sobakabot.model.GetOrCreateResult
import com.apuzanov.sobakabot.entity.Chat
import com.apuzanov.sobakabot.entity.User
import com.apuzanov.sobakabot.entity.UserChatStatistic
import com.apuzanov.sobakabot.enums.UserGender
import com.apuzanov.sobakabot.repository.UserRepository
import com.apuzanov.sobakabot.repository.UserStatisticRepository
import com.apuzanov.sobakabot.utils.userId
import com.apuzanov.sobakabot.utils.usernameOrName
import dev.inmo.tgbotapi.types.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userStatisticRepository: UserStatisticRepository
) {
    @Transactional
    fun getOrCreateUserFrom(telegramUser: dev.inmo.tgbotapi.types.chat.User): GetOrCreateResult<User> {
        val user = userRepository.findByTelegramId(telegramUser.id.userId)

        return if (user != null) {
            GetOrCreateResult(user, false)
        } else {
            GetOrCreateResult(
                userRepository.save(User(telegramUser.usernameOrName, telegramUser.id.userId, UserGender.IT)),
                true
            )
        }
    }

    fun getUser(userId: UserId): User? {
        return userRepository.findByTelegramId(userId.userId)
    }

    fun updateUsername(user: User, actualUsername: String) {
        userRepository.save(user.copy(username = actualUsername))
    }

    fun switchGender(user: User, newGender: UserGender) {
        userRepository.save(user.copy(gender = newGender))
    }

    @Transactional
    fun registerMessageInStatistic(user: User, chat: Chat) {
        val statistic = userStatisticRepository.findByChatAndUser(chat.id, user.id)

        if (statistic != null) {
            userStatisticRepository.save(
                statistic.copy(
                    messagesCount = statistic.messagesCount + 1,
                    lastActivity = LocalDateTime.now()
                )
            )
        } else {
            userStatisticRepository.save(
                UserChatStatistic(
                    chat.id,
                    user,
                    messagesCount = 1,
                    lastActivity = LocalDateTime.now()
                )
            )
        }
    }

    fun getStatistic(user: User, chat: Chat): UserChatStatistic? {
        return userStatisticRepository.findByChatAndUser(chat.id, user.id)
    }

    fun getTop(chat: Chat, limit: Short): List<UserChatStatistic> {
        return userStatisticRepository.findTopByMessagesCount(chat.id, limit)
    }

    fun getLatest(chat: Chat, limit: Short): List<UserChatStatistic> {
        return userStatisticRepository.findLatest(chat.id, limit)
    }

    fun deleteStatisticForChat(chatId: Short): Int {
        return userStatisticRepository.deleteByChatId(chatId)
    }
}
