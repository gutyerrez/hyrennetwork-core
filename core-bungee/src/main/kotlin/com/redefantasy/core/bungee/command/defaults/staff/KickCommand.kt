package com.redefantasy.core.bungee.command.defaults.staff

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.echo.packets.DisconnectUserPacket
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class KickCommand : CustomCommand("kick"), GroupCommandRestrictable {

    override fun getCommandRestriction() = CommandRestriction.CONSOLE_AND_GAME

    override fun getDescription() = "Chutar um jogador para fora do servidor."

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getAliases() = arrayOf("chutar")

    override fun getGroup() = Group.ADMINISTRATOR

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        if (!targetUser.isOnline()) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_ONLINE)
            return false
        }

        val reason = args.copyOfRange(1, args.size).joinToString(" ")

        val packet = DisconnectUserPacket()

        packet.userId = targetUser.getUniqueId()
        packet.reason = reason

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)

        commandSender.sendMessage(TextComponent("§eVocê chutou ${targetUser.name} para fora do servidor por: $reason."))
        return false
    }

}