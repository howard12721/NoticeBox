package jp.xhw.noticebox.infrastructure.external

import jp.xhw.noticebox.application.external.ItemClaimLogic
import jp.xhw.noticebox.infrastructure.extensions.toItemStack
import org.bukkit.Bukkit
import org.bukkit.entity.Item
import java.util.*

class ItemClaimLogicImpl : ItemClaimLogic {
    override fun claim(userId: UUID, items: List<ByteArray>) {
        val player = Bukkit.getPlayer(userId) ?: return
        for (itemData in items) {
            try {
                val over = player.inventory.addItem(itemData.toItemStack())
                for (value in over.values) {
                    player.world.spawn(player.location, Item::class.java) { item ->
                        item.itemStack = value
                        item.owner = player.uniqueId
                    }
                }
            } catch (_: IllegalStateException) {
            }
        }
    }

}