package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.domain.model.*
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class MakeAnnounceService(private val announceRepository: AnnounceRepository) {
    fun make(title: String, content: String, rewardMoney: Double, rewardItems: List<ByteArray>) {
        transaction {
            val announce = Announce(
                AnnounceId(UUID.randomUUID()),
                AnnounceTitle(title),
                AnnounceContent(content),
                AnnounceReward(Money(rewardMoney), rewardItems),
                LocalDateTime.now()
            )
            announceRepository.save(announce)
        }
    }
}