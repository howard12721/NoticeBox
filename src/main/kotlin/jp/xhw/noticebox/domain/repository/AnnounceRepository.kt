package jp.xhw.noticebox.domain.repository

import jp.xhw.noticebox.domain.model.Announce
import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.model.AnnounceReward
import jp.xhw.noticebox.domain.model.AnnounceSample
import java.time.LocalDateTime

interface AnnounceRepository {

    fun findById(announceId: AnnounceId): Announce?

    fun findPagedAnnounceSamples(offset: Long, limit: Int, after: LocalDateTime): List<AnnounceSample>

    fun findPagedAnnounceSamples(offset: Long, limit: Int): List<AnnounceSample>

    fun save(announce: Announce)

    fun delete(announceId: AnnounceId)

    fun getReward(announceId: AnnounceId): AnnounceReward

    fun count(): Long

    fun count(after: LocalDateTime): Long

}