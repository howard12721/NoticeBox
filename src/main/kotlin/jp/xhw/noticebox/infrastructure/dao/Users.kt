package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : UUIDTable() {

    val joinedAt: Column<LocalDateTime> = datetime("joined_at")

}