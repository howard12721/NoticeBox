package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FetchReadAnnouncesService(private val userRepository: UserRepository) {

    fun fetch(userId: UUID): List<UUID> {
        return transaction {
            userRepository.find(UserId(userId)).openedAnnounceIds.map(AnnounceId::value)
        }
    }

}