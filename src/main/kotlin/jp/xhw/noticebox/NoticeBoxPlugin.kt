package jp.xhw.noticebox

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import jp.xhw.noticebox.application.external.Economy
import jp.xhw.noticebox.application.external.ItemClaimLogic
import jp.xhw.noticebox.application.service.StoreUserFirstJoinService
import jp.xhw.noticebox.infrastructure.dao.AnnounceOpens
import jp.xhw.noticebox.infrastructure.dao.AnnounceRewards
import jp.xhw.noticebox.infrastructure.dao.Announces
import jp.xhw.noticebox.infrastructure.dao.Users
import jp.xhw.noticebox.infrastructure.external.ItemClaimLogicImpl
import jp.xhw.noticebox.infrastructure.external.VaultEconomy
import jp.xhw.noticebox.infrastructure.repository.AnnounceSqliteRepository
import jp.xhw.noticebox.infrastructure.repository.UserSqliteRepository
import jp.xhw.noticebox.presenter.command.Command
import jp.xhw.noticebox.presenter.listener.EventListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection
import java.time.LocalDateTime
import java.util.logging.Level

class NoticeBoxPlugin : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: NoticeBoxPlugin
        lateinit var economy: Economy
        lateinit var itemClaimLogic: ItemClaimLogic
    }

    val announceRepository = AnnounceSqliteRepository()
    val userRepository = UserSqliteRepository()

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))
    }

    override fun onEnable() {
        plugin = this

        Bukkit.getLogger().log(Level.INFO, "Plugin Enabled!")

        economy = VaultEconomy(this)
        itemClaimLogic = ItemClaimLogicImpl()

        if (!dataFolder.exists()) dataFolder.mkdir()
        val dbFile = File(dataFolder, "data.db")
        if (!dbFile.exists()) dbFile.createNewFile()

        Database.connect("jdbc:sqlite:${dbFile.path}", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            SchemaUtils.create(Announces)
            SchemaUtils.create(AnnounceRewards)
            SchemaUtils.create(AnnounceOpens)
            SchemaUtils.create(Users)
        }

        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(EventListener(), this)

        Command().register()

    }

    override fun onDisable() {}

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        StoreUserFirstJoinService(userRepository).store(event.player.uniqueId, LocalDateTime.now())
    }

}
