package com.redefantasy.core.bungee.command.defaults.staff

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.echo.packets.BroadcastMessagePacket
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author Gutyerrez
 */
class AnnounceCommand : CustomCommand("alerta"), GroupCommandRestrictable {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getDescription() = "Anunciar uma mensagem."

    override fun getArguments() = listOf(
        Argument("mensagem")
    )

    override fun getGroup() = Group.GAME_MASTER

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val message = args.joinToString(" ")

        val packet = BroadcastMessagePacket()

        val highestGroup = user!!.getHighestGroup()

        packet.message = ComponentBuilder()
            .append("\n")
            .append("${highestGroup.color}${highestGroup.prefix}§e: $message")
            .append("\n\n")
            .create()

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
        return false
    }

}