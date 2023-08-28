package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AnnounceOpen(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AnnounceOpen>(AnnounceOpens)

    var userId by AnnounceOpens.userId
    var announceId by AnnounceOpens.announceId

}