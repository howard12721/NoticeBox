package jp.xhw.noticebox.presenter.gui

import de.themoep.inventorygui.DynamicGuiElement
import de.themoep.inventorygui.InventoryGui
import de.themoep.inventorygui.StaticGuiElement
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.application.service.MakeAnnounceService
import jp.xhw.noticebox.infrastructure.extensions.toByteArray
import jp.xhw.noticebox.presenter.shared.AnnounceData
import jp.xhw.noticebox.presenter.shared.Constants
import net.wesjd.anvilgui.AnvilGUI
import net.wesjd.anvilgui.AnvilGUI.ResponseAction
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class CreateAnnounceMenu private constructor(
    plugin: NoticeBoxPlugin,
    menuTitle: String,
    private val announceData: AnnounceData
) {

    companion object {

        private val MENU_LAYOUT = arrayOf(
            "         ",
            " T M I   ",
            "        C"
        )

        fun create(plugin: NoticeBoxPlugin, title: String, announceData: AnnounceData): CreateAnnounceMenu {
            return CreateAnnounceMenu(plugin, title, announceData)
        }
    }

    private var menu: InventoryGui = InventoryGui(plugin, menuTitle, MENU_LAYOUT)

    init {
        this.menu.setCloseAction { _ -> false }
        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        filler.itemMeta = filler.itemMeta?.let {
            it.setDisplayName(" ")
            it
        }
        this.menu.setFiller(filler)

        this.menu.addElement(
            DynamicGuiElement('T') { _: HumanEntity? ->
                StaticGuiElement(
                    'T', ItemStack(Material.OAK_SIGN), label@{ click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@label true
                        AnvilGUI.Builder()
                            .plugin(NoticeBoxPlugin.plugin)
                            .title("§lタイトルを入力")
                            .itemLeft(ItemStack(Material.PAPER))
                            .text(announceData.title)
                            .onClick { slot, snapshot ->
                                if (slot != AnvilGUI.Slot.OUTPUT) return@onClick emptyList<ResponseAction>()
                                announceData.title = snapshot.text
                                return@onClick listOf(ResponseAction.close())
                            }
                            .onClose { close ->
                                this.menu.draw()
                                this.show(close.player)
                            }
                            .open(click.whoClicked as Player)
                        return@label true
                    },
                    "§lタイトルを設定",
                    "§a現在§f: " + announceData.title
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement('M') { _: HumanEntity? ->
                StaticGuiElement(
                    'M', ItemStack(Material.GOLD_INGOT), label@{ click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@label true
                        AnvilGUI.Builder()
                            .plugin(NoticeBoxPlugin.plugin)
                            .title("§lタイトルを入力")
                            .itemLeft(ItemStack(Material.GOLD_INGOT))
                            .text("与える金額を入力")
                            .onClick { slot, snapshot ->
                                if (slot != AnvilGUI.Slot.OUTPUT) return@onClick emptyList<ResponseAction>()
                                try {
                                    announceData.money = snapshot.text.toDouble()
                                } catch (e: NumberFormatException) {
                                    return@onClick listOf(ResponseAction.replaceInputText("無効な数値です"))
                                }
                                if (announceData.money < 0.0) return@onClick listOf(ResponseAction.replaceInputText("無効な数値です"))
                                return@onClick listOf(ResponseAction.close())
                            }
                            .onClose { close ->
                                this.menu.draw()
                                this.show(close.player)
                            }
                            .open(click.whoClicked as Player)
                        return@label true
                    },
                    "§l報酬としてお金を与える",
                    "§a現在§f: " + announceData.money
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement('I') { _: HumanEntity? ->
                StaticGuiElement(
                    'I', ItemStack(Material.CHEST), label@{ click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@label true
                        ItemStorageMenu.create(plugin, "与えるアイテムを入れてください", announceData, this.menu)
                            .show(click.whoClicked as Player)
                        return@label true
                    },
                    "§l報酬としてアイテムを与える",
                    "§a現在§f: " + announceData.items.size + "個のアイテム"
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement('C') { _: HumanEntity? ->
                StaticGuiElement(
                    'C', ItemStack(Material.LIME_STAINED_GLASS_PANE),
                    label@{ click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@label true
                        MakeAnnounceService(NoticeBoxPlugin.plugin.announceRepository).make(
                            announceData.title,
                            announceData.content.joinToString(Constants.PAGE_SEPARATOR),
                            announceData.money,
                            announceData.items.map { itemStack -> itemStack.toByteArray() })
                        (click.whoClicked as Player).let {
                            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f)
                            it.sendMessage(Constants.PREFIX + "お知らせを作成しました")
                        }
                        this.menu.close()
                        return@label true
                    },
                    "§l§aお知らせを作成する",
                )
            }
        )

    }

    fun show(player: Player) {
        menu.show(player)
    }

}