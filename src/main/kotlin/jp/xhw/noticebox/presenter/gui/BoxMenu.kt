package jp.xhw.noticebox.presenter.gui

import de.themoep.inventorygui.*
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.application.dto.AnnounceSampleDto
import jp.xhw.noticebox.application.exception.UserNotFoundException
import jp.xhw.noticebox.application.service.*
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*


class BoxMenu private constructor(plugin: NoticeBoxPlugin, menuTitle: String, private val player: Player) {

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

        fun create(plugin: NoticeBoxPlugin, title: String, player: Player): BoxMenu {
            return BoxMenu(plugin, title, player)
        }
    }

    private var currentPage = 1L

    private var currentSamples: List<AnnounceSampleDto> = listOf()
    private var numOfAnnounces: Long = 0L
    private var maxPage: Long = 1L
    private var readAnnounces: List<UUID> = listOf()


    private var menu: InventoryGui = InventoryGui(plugin, menuTitle, MENU_LAYOUT)

    init {
        this.menu.setCloseAction { _ -> false }

        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        filler.itemMeta = filler.itemMeta?.let {
            it.setDisplayName(Constants.GUI_TEXT_EMPTY)
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
                        Constants.GUI_TEXT_EMPTY
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
                    Constants.GUI_BUTTON_FIRST_PAGE
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
                        Constants.GUI_TEXT_EMPTY
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
                    Constants.GUI_BUTTON_PREVIOUS_PAGE
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
                        Constants.GUI_TEXT_EMPTY
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
                    Constants.GUI_BUTTON_NEXT_PAGE
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
                        Constants.GUI_TEXT_EMPTY
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
                    Constants.GUI_BUTTON_FIRST_PAGE
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
                    Constants.GUI_TEXT_EMPTY
                )
            }
            val sample = currentSamples[num]
            StaticGuiElement(
                'A',
                if (readAnnounces.contains(sample.announceId)) ItemStack(Material.BOOK) else createBrokenWrittenBook(),
                { _ ->
                    player.openBook(createBook(sample))
                    try {
                        val claimedReward = UserOpenAnnounceService(
                            NoticeBoxPlugin.plugin.userRepository,
                            NoticeBoxPlugin.plugin.announceRepository,
                            NoticeBoxPlugin.economy,
                            NoticeBoxPlugin.itemClaimLogic
                        ).open(player.uniqueId, sample.announceId)
                        claimedReward?.let {
                            if (claimedReward.money > 0.0 || claimedReward.items.isNotEmpty()) {
                                val messages = mutableListOf<String>()

                                messages.add(Constants.MESSAGE_SEPARATOR)
                                messages.add(Constants.GUI_TEXT_EMPTY)
                                if (claimedReward.money > 0.0) messages.add(Constants.GUI_MESSAGE_MONEY_REWARD.format(claimedReward.money.toInt()))
                                if (claimedReward.items.isNotEmpty()) messages.add(Constants.GUI_MESSAGE_ITEM_REWARD.format(claimedReward.items.size))
                                messages.add(Constants.GUI_TEXT_EMPTY)
                                messages.add(Constants.MESSAGE_SEPARATOR)

                                for (message in messages) {
                                    player.sendMessage(message)
                                }
                            }

                            player.playSound(
                                player,
                                Sound.ENTITY_PLAYER_LEVELUP,
                                1.0f,
                                1.0f
                            )
                        }
                    } catch (e: UserNotFoundException) {
                        this.menu.close()
                        this.menu.destroy()
                        player.sendMessage(Constants.GUI_ERROR_REWARD_USER_NOT_FOUND)
                    }
                    true
                },
                sample.title,
                "§7" + Constants.DATE_FORMAT.format(sample.createdAt)

            )
        }
    }

    private fun createBook(sample: AnnounceSampleDto): ItemStack {
        val itemStack = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta = itemStack.itemMeta!! as BookMeta
        val announceContentDto =
            FetchAnnounceContentService(NoticeBoxPlugin.plugin.announceRepository).fetch(sample.announceId)
        bookMeta.pages = announceContentDto?.content?.split(Constants.PAGE_SEPARATOR) ?: listOf(Constants.GUI_ERROR_ANNOUNCE_CONTENT_NOT_FOUND)
        bookMeta.setTitle((announceContentDto?.title ?: Constants.GUI_DEFAULT_ANNOUNCE_TITLE))
        bookMeta.author = Constants.GUI_BOOK_AUTHOR
        itemStack.itemMeta = bookMeta
        return itemStack
    }

    private fun update() {
        try {
            if (player.hasPermission(Constants.PERMISSION_VIEW_ALL)) {
                currentSamples = FetchAnnounceSamplesService(NoticeBoxPlugin.plugin.announceRepository).fetch(
                    ANNOUNCES_PER_PAGE * (currentPage - 1),
                    ANNOUNCES_PER_PAGE
                )

                numOfAnnounces = GetNumberOfAnnouncesService(NoticeBoxPlugin.plugin.announceRepository).get()
            } else {
                currentSamples = FetchUserAnnounceSamplesService(NoticeBoxPlugin.plugin.announceRepository, NoticeBoxPlugin.plugin.userRepository).fetch(
                    player.uniqueId,
                    ANNOUNCES_PER_PAGE * (currentPage - 1),
                    ANNOUNCES_PER_PAGE
                )

                numOfAnnounces = GetNumberOfUserAnnouncesService(NoticeBoxPlugin.plugin.announceRepository, NoticeBoxPlugin.plugin.userRepository).get(player.uniqueId)
            }
            maxPage = if (numOfAnnounces == 0L) 1L else (numOfAnnounces + ANNOUNCES_PER_PAGE - 1) / ANNOUNCES_PER_PAGE
            readAnnounces = FetchReadAnnouncesService(NoticeBoxPlugin.plugin.userRepository).fetch(player.uniqueId)

            menu.title = Constants.GUI_PAGE_TITLE_FORMAT.format(currentPage)
        } catch (e: UserNotFoundException) {
            this.menu.close()
            this.menu.destroy()
            player.sendMessage(Constants.GUI_ERROR_LOAD_ANNOUNCES_USER_NOT_FOUND)
        }
    }

    private fun createBrokenWrittenBook(): ItemStack {
        val response = ItemStack(Material.WRITTEN_BOOK)
        val meta = (response.itemMeta as BookMeta)
        meta.generation = null
        response.itemMeta = meta
        return response
    }

}