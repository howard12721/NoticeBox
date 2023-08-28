package jp.xhw.noticebox.presenter.listener

import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.presenter.gui.CreateAnnounceMenu
import jp.xhw.noticebox.presenter.shared.AnnounceData
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType

class EventListener : Listener {

    @EventHandler
    fun on(event: PlayerEditBookEvent) {
        if (!event.isSigning) return
        val bookKey =
            event.newBookMeta.persistentDataContainer.get(Constants.BOOK_KEY, PersistentDataType.STRING) ?: return
        if (bookKey != "announce") return
        CreateAnnounceMenu.create(
            NoticeBoxPlugin.plugin,
            "アナウンスをする",
            AnnounceData(event.newBookMeta.title ?: "", event.newBookMeta.pages, 0.0, mutableListOf())
        ).show(event.player)
    }

    @EventHandler
    fun on(event: PlayerInteractEvent) {
        if (event.action == Action.PHYSICAL) return
        val itemMeta = event.item?.itemMeta ?: return
        if (event.item?.type == Material.WRITABLE_BOOK) return
        if (itemMeta is BookMeta) {
            if (itemMeta.persistentDataContainer.getOrDefault(
                    Constants.BOOK_KEY,
                    PersistentDataType.STRING,
                    ""
                ) != "announce"
            ) return
            event.isCancelled = true
            CreateAnnounceMenu.create(
                NoticeBoxPlugin.plugin,
                "アナウンスをする",
                AnnounceData(itemMeta.title ?: "", itemMeta.pages, 0.0, mutableListOf())
            ).show(event.player)
        }
    }

}