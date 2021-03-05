package com.redefantasy.core.bungee.command.defaults.player.friend.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.friend.FriendCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class FriendDenyCommand : CustomCommand("negar") {

    override fun getDescription() = "Negar uma solicitação de amizade."

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getParent() = FriendCommand()

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

        if (!user!!.getFriendRequests().contains(targetUser)) {
            commandSender.sendMessage(TextComponent("§cVocê não possui uma solicitação de amizade deste jogador. Você pode enviar uma através do comando '/amigo adicionar ${targetUser.name}'."))
            return false
        }

        CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().delete(
            DeleteFriendUserDTO(
                targetUser.id,
                user.id
            )
        )

        commandSender.sendMessage(TextComponent("§aVocê recusou o pedido de amizade do usuário ${targetUser.getFancyName()}§a."))
        return true
    }

}