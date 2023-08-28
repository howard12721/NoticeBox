package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AnnounceReward(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AnnounceReward>(AnnounceRewards)

    var announceId by AnnounceRewards.announceId
    var money by AnnounceRewards.money
    var item by AnnounceRewards.item

}