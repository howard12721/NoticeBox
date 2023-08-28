package jp.xhw.noticebox.presenter.shared

import org.bukkit.inventory.ItemStack

data class AnnounceData(
    var title: String,
    var content: List<String>,
    var money: Double,
    var items: MutableList<ItemStack>
)