package jp.xhw.noticebox.infrastructure.repository

import jp.xhw.noticebox.domain.model.*
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import jp.xhw.noticebox.infrastructure.dao.AnnounceRewards
import jp.xhw.noticebox.infrastructure.dao.Announces
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import jp.xhw.noticebox.infrastructure.dao.Announce as AnnounceDao
import jp.xhw.noticebox.infrastructure.dao.AnnounceReward as AnnounceRewardDao

class AnnounceSqliteRepository : AnnounceRepository {

    override fun findById(announceId: AnnounceId): Announce? {
        val announce = AnnounceDao.findById(announceId.value) ?: return null

        return Announce(
            announceId,
            AnnounceTitle(announce.title),
            AnnounceContent(announce.content),
            getReward(announceId),
            announce.createdAt
        )
    }

    override fun findPagedAnnounceSamples(offset: Long, limit: Int): List<AnnounceSample> {
        val announces = Announces
            .slice(Announces.id, Announces.title, Announces.createdAt)
            .selectAll()
            .orderBy(Announces.createdAt, SortOrder.DESC)
            .limit(limit, offset = offset)
        return announces.map(this::convertToAnnounceSample).toList()
    }

    override fun save(announce: Announce) {
        val announceDao: AnnounceDao? = AnnounceDao.findById(announce.announceId.value)
        if (announceDao != null) {
            announceDao.title = announce.title.value
            announceDao.content = announce.content.value
            announceDao.createdAt = announce.createdAt
        } else {
            AnnounceDao.new(announce.announceId.value) {
                this.title = announce.title.value
                this.content = announce.content.value
                this.createdAt = announce.createdAt
            }
        }
        AnnounceRewards.deleteWhere { announceId eq announce.announceId.value }
        AnnounceRewards.batchInsert(announce.reward.items) { bytes ->
            this[AnnounceRewards.announceId] = announce.announceId.value
            this[AnnounceRewards.money] = 0.0
            this[AnnounceRewards.item] = ExposedBlob(bytes)
        }
        AnnounceRewards.insert {
            it[announceId] = announce.announceId.value
            it[money] = announce.reward.money.amount
            it[item] = ExposedBlob(byteArrayOf())
        }
    }

    override fun delete(announceId: AnnounceId) {
        AnnounceDao.findById(announceId.value)?.delete()
    }

    override fun getReward(announceId: AnnounceId): AnnounceReward {
        val rewards = AnnounceRewardDao.find { AnnounceRewards.announceId eq announceId.value }
        val items: MutableList<ByteArray> = mutableListOf()
        var money = 0.0

        for (reward in rewards) {
            money += reward.money
            if (reward.item.bytes.isNotEmpty()) items.add(reward.item.bytes)
        }

        return AnnounceReward(Money(money), items)
    }

    override fun count(): Long {
        return AnnounceDao.count()
    }

    private fun convertToAnnounceSample(resultRow: ResultRow): AnnounceSample {
        val uuid = resultRow[Announces.id].value

        return AnnounceSample(
            AnnounceId(uuid),
            AnnounceTitle(resultRow[Announces.title]),
            resultRow[Announces.createdAt]
        )
    }

}