package com.redefantasy.core.bungee.command.defaults.staff.server

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.staff.server.subcommands.ServerConnectCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author Gutyerrez
 */
class ServerCommand : CustomCommand("server"), GroupCommandRestrictable {

    override fun getDescription() = "Visualizar a lista de servidores disponíveis."

    override fun getSubCommands() = listOf(
        ServerConnectCommand()
    )

    override fun getGroup() = Group.MODERATOR

    override fun onCommand(
            commandSender: CommandSender,
            user: User?,
            args: Array<out String>
    ): Boolean {
        val servers = CoreProvider.Cache.Local.SERVERS.provide().fetchAll()

        val message = ComponentBuilder()
            .append("\n")
            .append("§2Servidores disponíveis (${servers.size}):")
            .append("\n\n")

        var color = ChatColor.WHITE

        servers.forEach {
            val onlineUsers = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByServer(it)

            message.append("$color ${it.displayName} §7(${onlineUsers.size} jogadores)")
                .event(ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/server conectar ${it.getName()}"
                ))
                .append("\n")

            if (color === ChatColor.WHITE) color = ChatColor.GRAY else color = ChatColor.WHITE
        }

        commandSender.sendMessage(*message.create())
        return true
    }

}