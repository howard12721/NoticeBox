package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.application.dto.AnnounceSampleDto
import jp.xhw.noticebox.application.exception.UserNotFoundException
import jp.xhw.noticebox.domain.model.User
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import jp.xhw.noticebox.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FetchUserAnnounceSamplesService(
    private val announceRepository: AnnounceRepository,
    private val userRepository: UserRepository
) {
    fun fetch(userId: UUID, offset: Long, limit: Int): List<AnnounceSampleDto> {
        return transaction {
            val user: User = userRepository.find(UserId(userId))?: throw UserNotFoundException(userId)
            val announceSampleList = announceRepository.findPagedAnnounceSamples(offset, limit, user.firstJoin)
            announceSampleList.map { sample ->
                AnnounceSampleDto(
                    sample.announceId.value,
                    sample.title.value,
                    sample.createdAt
                )
            }
        }
    }
}