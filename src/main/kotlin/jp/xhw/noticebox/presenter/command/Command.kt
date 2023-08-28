package jp.xhw.noticebox.presenter.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import jp.xhw.noticebox.NoticeBoxPlugin
import jp.xhw.noticebox.presenter.gui.BoxMenu
import jp.xhw.noticebox.presenter.gui.DeleteAnnounceMenu
import jp.xhw.noticebox.presenter.shared.Constants
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType


class Command {
    fun register() {
        CommandAPICommand("noticebox")
            .withSubcommand(
                CommandAPICommand("add")
                    .executesPlayer(PlayerCommandExecutor { sender, _ ->
                        val writableBook = ItemStack(Material.WRITABLE_BOOK)
                        val itemMeta: ItemMeta = writableBook.itemMeta!!
                        itemMeta.setDisplayName("§lお知らせの本文を入力")
                        itemMeta.lore = listOf("§a署名して決定")
                        itemMeta.persistentDataContainer.set(Constants.BOOK_KEY, PersistentDataType.STRING, "announce")
                        writableBook.itemMeta = itemMeta
                        if (sender.inventory.addItem(writableBook).isNotEmpty()) {
                            sender.sendMessage("§cインベントリが一杯です")
                        }
                    })
                    .withPermission("noticebox.add")
            )
            .withSubcommand(
                CommandAPICommand("remove")
                    .executesPlayer(PlayerCommandExecutor { sender, _ ->
                        DeleteAnnounceMenu.create(NoticeBoxPlugin.plugin, "削除メニュー").show(sender)
                    })
                    .withPermission("noticebox.remove")
            )
            .withSubcommand(
                CommandAPICommand("open")
                    .withOptionalArguments(PlayerArgument("target"))
                    .executesPlayer(PlayerCommandExecutor { sender, args ->
                        var target = sender
                        if (args.count() != 0) {
                            if (!sender.hasPermission("noticebox.open-other")) return@PlayerCommandExecutor
                            target = args.get(0) as Player
                        }
                        BoxMenu.create(NoticeBoxPlugin.plugin, "お知らせ", target).show(target)
                    })
                    .withPermission("noticebox.open")
            )
            .register()
    }
}