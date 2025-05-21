package jp.xhw.noticebox.presenter.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
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
                        itemMeta.setDisplayName(Constants.TITLE_ANNOUNCE_BOOK_INPUT)
                        itemMeta.lore = listOf(Constants.LORE_ANNOUNCE_BOOK_SIGN)
                        itemMeta.persistentDataContainer.set(Constants.BOOK_KEY, PersistentDataType.STRING, Constants.ANNOUNCE_BOOK_TAG_VALUE)
                        writableBook.itemMeta = itemMeta
                        if (sender.inventory.addItem(writableBook).isNotEmpty()) {
                            sender.sendMessage(Constants.MESSAGE_INVENTORY_FULL)
                        }
                    })
                    .withPermission(Constants.PERMISSION_ADD)
            )
            .withSubcommand(
                CommandAPICommand("remove")
                    .executesPlayer(PlayerCommandExecutor { sender, _ ->
                        DeleteAnnounceMenu.create(NoticeBoxPlugin.plugin, Constants.MENU_TITLE_DELETE).show(sender)
                    })
                    .withPermission(Constants.PERMISSION_REMOVE)
            )
            .withSubcommand(
                CommandAPICommand("open")
                    .withOptionalArguments(PlayerArgument("target"))
                    .executesPlayer(PlayerCommandExecutor { sender, args ->
                        openNoticeBoxLogic(sender, args)
                    })
                    .executes(CommandExecutor { sender, args ->
                        openNoticeBoxLogic(sender, args)
                    })
                    .withPermission(Constants.PERMISSION_OPEN)
            )
            .register()
    }

    private fun openNoticeBoxLogic(sender: org.bukkit.command.CommandSender, args: dev.jorel.commandapi.executors.CommandArguments) {
        val targetPlayer: Player?

        if (args.count() == 0) {
            // No target player specified in arguments
            if (sender is Player) {
                targetPlayer = sender
            } else {
                // Console or other non-player sender trying to open for self
                sender.sendMessage(Constants.MESSAGE_SPECIFY_PLAYER_FOR_CONSOLE)
                return
            }
        } else {
            // Target player is specified in arguments
            if (!sender.hasPermission(Constants.PERMISSION_OPEN_OTHER)) {
                sender.sendMessage(Constants.MESSAGE_NO_PERMISSION_OPEN_OTHER)
                return
            }
            targetPlayer = args.get("target") as? Player
            if (targetPlayer == null) {
                sender.sendMessage(Constants.MESSAGE_PLAYER_NOT_FOUND)
                return
            }
        }

        BoxMenu.create(NoticeBoxPlugin.plugin, Constants.MENU_TITLE_BOX, targetPlayer).show(targetPlayer)
    }
}