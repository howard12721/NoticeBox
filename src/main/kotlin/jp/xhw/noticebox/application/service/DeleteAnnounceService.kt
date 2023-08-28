package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DeleteAnnounceService(private val announceRepository: AnnounceRepository) {
    fun delete(id: UUID) {
        transaction {
            announceRepository.delete(AnnounceId(id))
        }
    }
}