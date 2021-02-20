package com.redefantasy.core.bungee.command.defaults.staff

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.echo.packets.StaffMessagePacket
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

/**
 * @author Gutyerrez
 */
class StaffChatCommand : CustomCommand("s"), GroupCommandRestrictable {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getDescription() = "Enviar uma mensagem no bate-papo da equipe."

    override fun getArguments() = listOf(
        Argument("mensagem")
    )

    override fun getGroup() = Group.HELPER

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        user ?: return false

        val message = args.joinToString(" ")

        val packet = StaffMessagePacket()

        packet.stafferId = user.getUniqueId()
        packet.bukkitApplication = user.getConnectedBukkitApplication()
        packet.message = message

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
        return false
    }

}