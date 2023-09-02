package jp.xhw.noticebox.presenter.gui

import de.themoep.inventorygui.*
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.application.dto.AnnounceSampleDto
import jp.xhw.noticebox.application.service.FetchAnnounceSamplesService
import jp.xhw.noticebox.application.service.GetNumberOfAnnouncesService
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class DeleteAnnounceMenu private constructor(plugin: NoticeBoxPlugin, menuTitle: String) {

    companion object {

        private const val ANNOUNCES_PER_PAGE = 28

        private val MENU_LAYOUT = arrayOf(
            "         ",
            " AAAAAAA ",
            " AAAAAAA ",
            " AAAAAAA ",
            " AAAAAAA ",
            "fp     nl"
        )

        fun create(plugin: NoticeBoxPlugin, title: String): DeleteAnnounceMenu {
            return DeleteAnnounceMenu(plugin, title)
        }
    }

    private var currentPage = 1L

    private var currentSamples: List<AnnounceSampleDto> = listOf()
    private var numOfAnnounces: Long = 0L
    private var maxPage: Long = 1L


    private var menu: InventoryGui = InventoryGui(plugin, menuTitle, MENU_LAYOUT)

    init {
        this.menu.setCloseAction { _ -> false }

        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        filler.itemMeta = filler.itemMeta?.let {
            it.setDisplayName(" ")
            it
        }
        this.menu.setFiller(filler)

        val group = GuiElementGroup('A')
        for (i in 0..27) {
            group.addElement(getBookElement(i))
        }
        this.menu.addElement(group)

        this.menu.addElement(
            DynamicGuiElement(
                'f'
            ) { _ ->
                if (currentPage == 1L) {
                    return@DynamicGuiElement StaticGuiElement(
                        'f',
                        ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                        " "
                    )
                }
                StaticGuiElement(
                    'f',
                    ItemStack(Material.OAK_SIGN),
                    { click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@StaticGuiElement true
                        currentPage = 1
                        update()
                        click.gui.draw()
                        true
                    },
                    "最初のページへ"
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement(
                'p'
            ) { _ ->
                if (currentPage == 1L) {
                    return@DynamicGuiElement StaticGuiElement(
                        'p',
                        ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                        " "
                    )
                }
                StaticGuiElement(
                    'p',
                    ItemStack(Material.ARROW),
                    { click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@StaticGuiElement true
                        currentPage = (currentPage - 1).coerceAtLeast(1)
                        update()
                        click.gui.draw()
                        true
                    },
                    "前のページへ"
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement(
                'n'
            ) { _ ->
                if (currentPage == maxPage) {
                    return@DynamicGuiElement StaticGuiElement(
                        'n',
                        ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                        " "
                    )
                }
                StaticGuiElement(
                    'n',
                    ItemStack(Material.ARROW),
                    { click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@StaticGuiElement true
                        currentPage = (currentPage + 1).coerceAtMost(maxPage)
                        update()
                        click.gui.draw()
                        true
                    },
                    "次のページへ"
                )
            }
        )

        this.menu.addElement(
            DynamicGuiElement(
                'l'
            ) { _ ->
                if (currentPage == maxPage) {
                    return@DynamicGuiElement StaticGuiElement(
                        'l',
                        ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                        " "
                    )
                }
                StaticGuiElement(
                    'l',
                    ItemStack(Material.OAK_SIGN),
                    { click ->
                        if (click.type == ClickType.DOUBLE_CLICK) return@StaticGuiElement true
                        currentPage = maxPage
                        update()
                        click.gui.draw()
                        true
                    },
                    "最初のページへ"
                )
            }
        )
    }

    fun show(player: Player) {
        update()
        menu.show(player)
    }

    private fun getBookElement(num: Int): GuiElement {
        return DynamicGuiElement('A') { _ ->
            if (num + 1 > currentSamples.size) {
                return@DynamicGuiElement StaticGuiElement(
                    'A',
                    ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                    ""
                )
            }
            val sample = currentSamples[num]
            StaticGuiElement(
                'A',
                ItemStack(Material.BOOK),
                { click ->
                    DeleteConfirmMenu.create(NoticeBoxPlugin.plugin, "削除を確認", sample, this)
                        .show(click.whoClicked as Player)
                    true
                },
                sample.title,
                "§7" + Constants.DATE_FORMAT.format(sample.createdAt)

            )
        }
    }

    private fun update() {
        currentSamples = FetchAnnounceSamplesService(NoticeBoxPlugin.plugin.announceRepository).fetch(
            ANNOUNCES_PER_PAGE * (currentPage - 1),
            ANNOUNCES_PER_PAGE
        )
        numOfAnnounces = GetNumberOfAnnouncesService(NoticeBoxPlugin.plugin.announceRepository).get()
        maxPage = if (numOfAnnounces == 0L) 1L else (numOfAnnounces + ANNOUNCES_PER_PAGE - 1) / ANNOUNCES_PER_PAGE

        menu.title = "ページ: $currentPage"
    }

}