package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.application.dto.AnnounceContentDto
import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FetchAnnounceContentService(private val announceRepository: AnnounceRepository) {

    fun fetch(announceId: UUID): AnnounceContentDto? {
        return transaction {
            val announce = announceRepository.findById(AnnounceId(announceId)) ?: return@transaction null
            AnnounceContentDto(
                announce.announceId.value,
                announce.title.value,
                announce.content.value,
                announce.createdAt
            )
        }
    }

}