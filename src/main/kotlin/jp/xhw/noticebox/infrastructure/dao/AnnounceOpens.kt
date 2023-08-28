package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object AnnounceOpens : IntIdTable() {

    val userId: Column<UUID> = uuid("user_id")
    val announceId: Column<UUID> = uuid("announce_id").references(Announces.id)

}