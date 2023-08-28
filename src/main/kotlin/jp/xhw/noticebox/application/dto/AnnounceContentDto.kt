package jp.xhw.noticebox.application.dto

import java.time.LocalDateTime
import java.util.*

class AnnounceContentDto(val announceId: UUID, val title: String, val content: String, val createdAt: LocalDateTime)