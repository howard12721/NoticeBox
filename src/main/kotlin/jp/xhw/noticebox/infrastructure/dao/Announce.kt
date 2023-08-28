package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Announce(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Announce>(Announces)

    var title by Announces.title
    var content by Announces.content
    var createdAt by Announces.createdAt

}