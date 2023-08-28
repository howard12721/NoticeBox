package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Announces : UUIDTable() {

    val title: Column<String> = text("title")
    val content: Column<String> = text("content")
    val createdAt: Column<LocalDateTime> = datetime("created_at")
}