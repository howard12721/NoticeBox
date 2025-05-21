package jp.xhw.noticebox.presenter.listener

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventListenerTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: NoticeBoxPlugin
    private lateinit var player: PlayerMock
    private lateinit var eventListener: EventListener

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(NoticeBoxPlugin::class.java)
        player = server.addPlayer()
        eventListener = EventListener()
        server.pluginManager.registerEvents(eventListener, plugin)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `on PlayerEditBookEvent with signing and correct PDC tag opens CreateAnnounceMenu`() {
        // Prepare the book meta
        val book = ItemStack(Material.WRITABLE_BOOK)
        val newMeta = book.itemMeta as BookMeta
        newMeta.title = "Test Announce Title"
        newMeta.setPages("Page 1", "Page 2")
        newMeta.persistentDataContainer.set(Constants.BOOK_KEY, PersistentDataType.STRING, Constants.ANNOUNCE_BOOK_TAG_VALUE)
        book.itemMeta = newMeta

        // Create and fire the event
        val event = PlayerEditBookEvent(player, 0, ItemStack(Material.WRITABLE_BOOK).itemMeta as BookMeta, newMeta, true)
        server.pluginManager.callEvent(event) // Alternative: eventListener.on(event) if direct call is preferred

        // Verify that the player has an open inventory with the correct title
        // This assumes CreateAnnounceMenu opens an inventory with its passed title.
        // InventoryGui uses the title passed to its constructor for the inventory view.
        assertNotNull(player.openInventory, "Player should have an open inventory.")
        assertEquals(Constants.MENU_TITLE_CREATE_ANNOUNCE, player.openInventory.title, "Opened inventory title is incorrect.")
    }

    @Test
    fun `on PlayerEditBookEvent not signing does not open menu`() {
        val book = ItemStack(Material.WRITABLE_BOOK)
        val newMeta = book.itemMeta as BookMeta
        newMeta.title = "Test Announce Title"
        newMeta.setPages("Page 1")
        newMeta.persistentDataContainer.set(Constants.BOOK_KEY, PersistentDataType.STRING, Constants.ANNOUNCE_BOOK_TAG_VALUE)
        book.itemMeta = newMeta

        val event = PlayerEditBookEvent(player, 0, ItemStack(Material.WRITABLE_BOOK).itemMeta as BookMeta, newMeta, false) // isSigning = false
        server.pluginManager.callEvent(event)

        assertNull(player.openInventory, "Player should not have an open inventory if not signing.")
    }

    @Test
    fun `on PlayerEditBookEvent with incorrect PDC tag does not open menu`() {
        val book = ItemStack(Material.WRITABLE_BOOK)
        val newMeta = book.itemMeta as BookMeta
        newMeta.title = "Test Announce Title"
        newMeta.setPages("Page 1")
        newMeta.persistentDataContainer.set(Constants.BOOK_KEY, PersistentDataType.STRING, "not_announce_book") // Incorrect tag
        book.itemMeta = newMeta

        val event = PlayerEditBookEvent(player, 0, ItemStack(Material.WRITABLE_BOOK).itemMeta as BookMeta, newMeta, true)
        server.pluginManager.callEvent(event)

        assertNull(player.openInventory, "Player should not have an open inventory if PDC tag is incorrect.")
    }

    @Test
    fun `on PlayerEditBookEvent with missing PDC tag does not open menu`() {
        val book = ItemStack(Material.WRITABLE_BOOK)
        val newMeta = book.itemMeta as BookMeta // No PDC tag set
        newMeta.title = "Test Announce Title"
        newMeta.setPages("Page 1")
        book.itemMeta = newMeta

        val event = PlayerEditBookEvent(player, 0, ItemStack(Material.WRITABLE_BOOK).itemMeta as BookMeta, newMeta, true)
        server.pluginManager.callEvent(event)

        assertNull(player.openInventory, "Player should not have an open inventory if PDC tag is missing.")
    }
}
