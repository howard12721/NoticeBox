package jp.xhw.noticebox.infrastructure.dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.util.*

object AnnounceRewards : IntIdTable() {

    val announceId: Column<UUID> = uuid("announce_id").references(Announces.id)
    val money: Column<Double> = double("money")
    val item: Column<ExposedBlob> = blob("item")

}