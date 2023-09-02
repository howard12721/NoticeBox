package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.domain.model.User
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class StoreUserFirstJoinService(private val userRepository: UserRepository) {
    fun store(userId: UUID, date: LocalDateTime) {
        transaction {
            val user = userRepository.find(UserId(userId))
            if (user == null) {
                userRepository.save(User(UserId(userId), mutableListOf(), date))
            }
        }
    }
}