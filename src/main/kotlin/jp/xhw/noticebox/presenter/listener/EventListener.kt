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
        val bookKeyValue =
            event.newBookMeta.persistentDataContainer.get(Constants.BOOK_KEY, PersistentDataType.STRING) ?: return
        if (bookKeyValue != Constants.ANNOUNCE_BOOK_TAG_VALUE) return
        CreateAnnounceMenu.create(
            NoticeBoxPlugin.plugin,
            Constants.MENU_TITLE_CREATE_ANNOUNCE,
            AnnounceData(event.newBookMeta.title ?: "", event.newBookMeta.pages, 0.0, mutableListOf())
        ).show(event.player)
    }

    @EventHandler
    fun on(event: PlayerInteractEvent) {
        // Ignore physical actions (e.g., stepping on a pressure plate)
        if (event.action == Action.PHYSICAL) return

        // Ensure there's an item involved in the interaction
        val item = event.item ?: return
        val itemMeta = item.itemMeta ?: return

        // This event handles interactions with books that are already written (signed)
        // and tagged as announcement books.
        // If the book is a WRITABLE_BOOK (unsigned), vanilla behavior should occur (opening the book for writing).
        // The creation of an announcement from a WRITABLE_BOOK is handled by PlayerEditBookEvent upon signing.
        if (item.type == Material.WRITABLE_BOOK) return

        // Proceed only if the item is a written book and its meta is BookMeta
        if (item.type == Material.WRITTEN_BOOK && itemMeta is BookMeta) {
            // Check for the custom NBT tag that identifies this book as an announcement book.
            // The value "announce" is associated with Constants.BOOK_KEY for such books.
            val bookKeyValue = itemMeta.persistentDataContainer.getOrDefault(
                Constants.BOOK_KEY,
                PersistentDataType.STRING,
                ""
            )

            if (bookKeyValue == Constants.ANNOUNCE_BOOK_TAG_VALUE) {
                // Cancel the default behavior (e.g., reading the written book)
                event.isCancelled = true
                // Open the CreateAnnounceMenu to allow editing/re-purposing this announcement book.
                // The content (title, pages) from the existing written book is used to populate the menu.
                CreateAnnounceMenu.create(
                    NoticeBoxPlugin.plugin,
                    Constants.MENU_TITLE_CREATE_ANNOUNCE,
                    AnnounceData(itemMeta.title ?: "", itemMeta.pages, 0.0, mutableListOf())
                ).show(event.player)
            }
        }
    }

}