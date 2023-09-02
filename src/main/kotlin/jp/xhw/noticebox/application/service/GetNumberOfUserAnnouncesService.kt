package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.application.exception.UserNotFoundException
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import jp.xhw.noticebox.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class GetNumberOfUserAnnouncesService(private val announceRepository: AnnounceRepository, private val userRepository: UserRepository) {
    fun get(userId: UUID): Long {
        return transaction {
            val user = userRepository.find(UserId(userId))?: throw UserNotFoundException(userId)
            announceRepository.count(user.firstJoin)
        }
    }
}