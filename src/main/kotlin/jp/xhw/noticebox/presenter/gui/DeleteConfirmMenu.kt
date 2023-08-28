package jp.xhw.noticebox.presenter.gui

import de.themoep.inventorygui.InventoryGui
import de.themoep.inventorygui.StaticGuiElement
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.application.dto.AnnounceSampleDto
import jp.xhw.noticebox.application.service.DeleteAnnounceService
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DeleteConfirmMenu private constructor(
    plugin: NoticeBoxPlugin,
    menuTitle: String,
    sample: AnnounceSampleDto,
    fallback: DeleteAnnounceMenu
) {

    companion object {

        private val MENU_LAYOUT = arrayOf(
            "         ",
            "    B    ",
            "         ",
            "  D   C  ",
            "         "
        )

        fun create(
            plugin: NoticeBoxPlugin,
            title: String,
            sample: AnnounceSampleDto,
            fallback: DeleteAnnounceMenu
        ): DeleteConfirmMenu {
            return DeleteConfirmMenu(plugin, title, sample, fallback)
        }
    }

    private var menu: InventoryGui = InventoryGui(plugin, menuTitle, MENU_LAYOUT)

    init {
        this.menu.setCloseAction { _ -> false }

        this.menu.addElement(
            StaticGuiElement(
                'B',
                ItemStack(Material.BOOK),
                sample.title,
                "§7" + Constants.DATE_FORMAT.format(sample.createdAt)
            )
        )

        this.menu.addElement(
            StaticGuiElement(
                'D',
                ItemStack(Material.REDSTONE_BLOCK),
                { click ->
                    DeleteAnnounceService(NoticeBoxPlugin.plugin.announceRepository).delete(sample.announceId)
                    (click.whoClicked as Player).let {
                        it.playSound(it.location, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1f)
                        it.sendMessage(Constants.PREFIX + "お知らせ「${sample.title}」を削除しました")
                    }
                    fallback.show(click.whoClicked as Player)
                    true
                },
                "§l§c削除する"
            )
        )

        this.menu.addElement(
            StaticGuiElement(
                'C',
                ItemStack(Material.BARRIER),
                { click ->
                    fallback.show(click.whoClicked as Player)
                    true
                },
                "§l§cキャンセル"
            )
        )

    }

    fun show(player: Player) {
        menu.show(player)
    }

}