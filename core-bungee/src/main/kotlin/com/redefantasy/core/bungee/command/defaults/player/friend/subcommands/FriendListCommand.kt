package com.redefantasy.core.bungee.command.defaults.player.friend.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.friend.FriendCommand
import com.redefantasy.core.shared.misc.utils.NumberUtils
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class FriendListCommand : CustomCommand("listar") {

    override fun getParent() = FriendCommand()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val page = if (args.size == 1 && NumberUtils.isValidInteger(args[0])) args[0].toInt() else 0
        val pages = user!!.getFriends().size / 10

        val message = ComponentBuilder()
            .append("\n")
            .append("§aAmigos - Página $page/$pages")
            .append("\n\n")

        val friends = user.getFriends()
            .stream()
            .sorted { user1, user2 -> user2.isOnline().compareTo(user1.isOnline()) }
            .collect(Collectors.toList())
            .subList(if (page == 0) page else page * 10, 10)

        friends.forEachIndexed { index, it ->
            val group = it.getHighestGroup()

            message.append("${group.getColoredPrefix()}${it.name}")
                .append(" ")
                .append(if (it.isOnline()) "§7está offline." else "§aestá online.")

            if (index + 1 < friends.size) message.append("\n")
        }

        commandSender.sendMessage(*message.create())
        return true
    }

}