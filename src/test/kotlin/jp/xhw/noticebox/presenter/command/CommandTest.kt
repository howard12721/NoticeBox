package jp.xhw.noticebox.presenter.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CommandTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: NoticeBoxPlugin
    private lateinit var player: PlayerMock
    private lateinit var command: jp.xhw.noticebox.presenter.command.Command

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(NoticeBoxPlugin::class.java)
        player = server.addPlayer()
        // Register commands
        command = jp.xhw.noticebox.presenter.command.Command()
        command.register()
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `add command gives player a writable book with correct meta and PDC tag`() {
        // Ensure player has permission
        player.addAttachment(plugin, Constants.PERMISSION_ADD, true)

        // Execute the command
        player.performCommand("noticebox add")

        // Verify player received a writable book
        val inventory = player.inventory
        val book = inventory.contents.find { it?.type == Material.WRITABLE_BOOK }
        assertNotNull(book, "Player should have received a writable book")
        assertNotEquals(0, inventory.contents.count { it?.type == Material.WRITABLE_BOOK }, "Player should have one writable book in inventory.")


        val bookMeta = book!!.itemMeta as? BookMeta
        assertNotNull(bookMeta, "The item should have BookMeta")

        // Verify display name and lore
        assertEquals(Constants.TITLE_ANNOUNCE_BOOK_INPUT, bookMeta!!.displayName, "Book display name is incorrect")
        assertEquals(listOf(Constants.LORE_ANNOUNCE_BOOK_SIGN), bookMeta.lore, "Book lore is incorrect")

        // Verify PersistentDataContainer tag
        val namespacedKey = Constants.BOOK_KEY
        assertTrue(bookMeta.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING), "Book meta should have the BOOK_KEY tag")
        assertEquals(Constants.ANNOUNCE_BOOK_TAG_VALUE, bookMeta.persistentDataContainer.get(namespacedKey, PersistentDataType.STRING), "Book meta BOOK_KEY tag value is incorrect")
    }

    @Test
    fun `add command informs player if inventory is full`() {
        // Ensure player has permission
        player.addAttachment(plugin, Constants.PERMISSION_ADD, true)

        // Fill player's inventory completely
        val fillerItem = ItemStack(Material.STONE)
        for (i in 0 until player.inventory.size) {
            player.inventory.setItem(i, fillerItem.clone())
        }

        // Execute the command
        player.performCommand("noticebox add")

        // Verify player received the inventory full message
        // Note: PlayerMock.nextMessage() retrieves messages sent to the player
        assertEquals(Constants.MESSAGE_INVENTORY_FULL, player.nextMessage(), "Player should be notified that inventory is full")

        // Verify no new writable book was added (as it couldn't fit)
        var bookFound = false
        for (item in player.inventory.contents) {
            if (item != null && item.type == Material.WRITABLE_BOOK) {
                val bookMeta = item.itemMeta as? BookMeta
                if (bookMeta != null && Constants.BOOK_KEY.key == "book_key" && bookMeta.persistentDataContainer.has(Constants.BOOK_KEY, PersistentDataType.STRING)) {
                     //This check is to ensure we are not picking up a book that was already there from a previous test or setup
                    if(bookMeta.persistentDataContainer.get(Constants.BOOK_KEY, PersistentDataType.STRING) == Constants.ANNOUNCE_BOOK_TAG_VALUE){
                        bookFound = true
                        break
                    }
                }
            }
        }
         assertFalse(bookFound, "No new announcement book should be added to a full inventory.")
    }
}
