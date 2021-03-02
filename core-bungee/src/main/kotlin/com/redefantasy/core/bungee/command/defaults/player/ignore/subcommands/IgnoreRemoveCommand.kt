package com.redefantasy.core.bungee.command.defaults.player.ignore.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.ignore.IgnoreCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class IgnoreRemoveCommand : CustomCommand("remover") {

    override fun getDescription() = "Deixa de ignorar um usuário em específico."

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getParent() = IgnoreCommand()

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean? {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        if (!targetUser.isOnline()) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_ONLINE)
            return false
        }

        val ignoredUser = user!!.getIgnoredUsers().stream().filter { it === targetUser }.findFirst().orElse(null)

        if (ignoredUser === null) {
            commandSender.sendMessage(TextComponent("§cEste usuário já não está em sua lista de ignorados."))
            return false
        }

        CoreProvider.Repositories.Mongo.USERS_IGNORED_REPOSITORY.provide().delete(
            DeleteIgnoredUserDTO(
                user.id,
                ignoredUser.id
            )
        )

        commandSender.sendMessage(TextComponent("§aUsuário removido da sua lista de ignorados."))
        return true
    }

}