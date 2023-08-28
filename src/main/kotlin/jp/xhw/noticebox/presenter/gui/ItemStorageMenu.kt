package jp.xhw.noticebox.presenter.gui

import de.themoep.inventorygui.GuiStorageElement
import de.themoep.inventorygui.InventoryGui
import de.themoep.inventorygui.StaticGuiElement
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.presenter.shared.AnnounceData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class ItemStorageMenu private constructor(
    plugin: NoticeBoxPlugin,
    menuTitle: String,
    private val announceData: AnnounceData,
    private val fallback: InventoryGui
) {

    companion object {

        private val MENU_LAYOUT = arrayOf(
            "IIIIIIIII",
            "IIIIIIIII",
            "CIIIIIIII"
        )

        fun create(
            plugin: NoticeBoxPlugin,
            title: String,
            announceData: AnnounceData,
            fallback: InventoryGui
        ): ItemStorageMenu {
            return ItemStorageMenu(plugin, title, announceData, fallback)
        }
    }

    private var menu: InventoryGui = InventoryGui(plugin, menuTitle, MENU_LAYOUT)
    private val itemInventory = Bukkit.createInventory(null, InventoryType.CHEST, "ここにアイテムを入れる")

    init {
        announceData.items.forEach { itemStack -> itemInventory.addItem(itemStack) }
        this.menu.addElement(GuiStorageElement('I', itemInventory))
        this.menu.addElement(
            StaticGuiElement(
                'C', ItemStack(Material.ARROW), { click ->
                    announceData.items.clear()
                    itemInventory.forEach { itemStack -> itemStack?.let { announceData.items.add(itemStack) } }
                    fallback.show(click.whoClicked)
                    true
                },
                "§c§l戻る"
            )
        )
        this.menu.setCloseAction { _ -> false }
    }

    fun show(player: Player) {
        menu.show(player)
    }

}