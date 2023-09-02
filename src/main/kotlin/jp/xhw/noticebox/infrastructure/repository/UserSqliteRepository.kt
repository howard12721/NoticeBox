package jp.xhw.noticebox.infrastructure.repository

import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.model.User
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.UserRepository
import jp.xhw.noticebox.infrastructure.dao.AnnounceOpens
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import jp.xhw.noticebox.infrastructure.dao.AnnounceOpen as AnnounceOpensDao
import jp.xhw.noticebox.infrastructure.dao.User as UserDao

class UserSqliteRepository : UserRepository {

    override fun find(userId: UserId): User? {
        val user = UserDao.findById(userId.value) ?: return null
        val openedAnnounces = mutableListOf<AnnounceId>()
        AnnounceOpensDao
            .find { AnnounceOpens.userId eq userId.value }
            .forEach { announceOpen -> openedAnnounces.add(AnnounceId(announceOpen.announceId)) }
        return User(userId, openedAnnounces, user.joinedAt)
    }

    override fun save(user: User) {
        val userDao = UserDao.findById(user.userId.value)
        if (userDao == null) {
            UserDao.new(user.userId.value) {
                this.joinedAt = user.firstJoin
            }
        }
        AnnounceOpens.deleteWhere { userId eq user.userId.value }
        val list = mutableListOf<Pair<UserId, AnnounceId>>()
        for (announceId in user.openedAnnounceIds) {
            list.add(Pair(user.userId, announceId))
        }
        AnnounceOpens.batchInsert(list) {
            this[AnnounceOpens.userId] = it.first.value
            this[AnnounceOpens.announceId] = it.second.value
        }
    }

}