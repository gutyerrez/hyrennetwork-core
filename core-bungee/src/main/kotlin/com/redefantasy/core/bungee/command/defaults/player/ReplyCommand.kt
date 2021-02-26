package com.redefantasy.core.bungee.command.defaults.player

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.echo.packets.TellPacket
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class ReplyCommand : CustomCommand("r") {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getDescription() = "Responder uma mensagem privada de um jogador."

    override fun getArguments() = listOf(
            Argument("mensagem")
    )

    override fun onCommand(
            commandSender: CommandSender,
            user: User?,
            args: Array<out String>
    ): Boolean? {
        if (user!!.directMessage === null || !user!!.directMessage!!.isOnline()) {
            commandSender.sendMessage(TextComponent("§cVocê não possui ninguém para responder."))
            return false
        }

        user.directMessage = user.directMessage

        val message = args.joinToString(" ")

        val packet = TellPacket()

        packet.senderId = user.getUniqueId()
        packet.receiverId = user.directMessage!!.getUniqueId()
        packet.message = message

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
        return true
    }

}