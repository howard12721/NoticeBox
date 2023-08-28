package jp.xhw.noticebox.domain.model

import java.time.LocalDateTime
import java.util.*

class Announce(
    val announceId: AnnounceId,
    val title: AnnounceTitle,
    val content: AnnounceContent,
    val reward: AnnounceReward,
    val createdAt: LocalDateTime
)

data class AnnounceId(val value: UUID)

data class AnnounceTitle(val value: String)

data class AnnounceContent(val value: String)

data class AnnounceReward(val money: Money, val items: List<ByteArray>)

data class AnnounceSample(val announceId: AnnounceId, val title: AnnounceTitle, val createdAt: LocalDateTime)