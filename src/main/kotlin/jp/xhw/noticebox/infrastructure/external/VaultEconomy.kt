package jp.xhw.noticebox.infrastructure.external

import jp.xhw.noticebox.application.external.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider
import java.util.*

class VaultEconomy(plugin: Plugin) : Economy {
    private var economy: net.milkbowl.vault.economy.Economy? = null

    init {
        if (plugin.server.pluginManager.getPlugin("Vault") == null) {
            plugin.server.pluginManager.disablePlugin(plugin)
            throw RuntimeException("Couldn't find vault economy")
        }

        val rsp: RegisteredServiceProvider<net.milkbowl.vault.economy.Economy>? =
            plugin.server.servicesManager.getRegistration(net.milkbowl.vault.economy.Economy::class.java)
        economy = rsp?.provider
    }

    override fun giveMoney(userId: UUID, amount: Double) {
        economy?.depositPlayer(Bukkit.getOfflinePlayer(userId), amount)
    }

}