package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.domain.repository.AnnounceRepository
import org.jetbrains.exposed.sql.transactions.transaction

class GetNumberOfAnnouncesService(private val announceRepository: AnnounceRepository) {
    fun get(): Long {
        return transaction {
            announceRepository.count()
        }
    }
}