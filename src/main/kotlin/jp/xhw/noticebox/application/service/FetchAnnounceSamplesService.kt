package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.application.dto.AnnounceSampleDto
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import org.jetbrains.exposed.sql.transactions.transaction

class FetchAnnounceSamplesService(private val announceRepository: AnnounceRepository) {
    fun fetch(offset: Long, limit: Int): List<AnnounceSampleDto> {
        return transaction {
            val announceSampleList = announceRepository.findPagedAnnounceSamples(offset, limit)
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