package com.redefantasy.core.bungee.command.defaults.staff.account.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.dto.UpdateUserByIdDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class AccountChangeDiscordIdCommand : CustomCommand("discord") {

    override fun getDescription() = "Alterar o id do discord de um usuário."

    override fun getArguments() = listOf(
        Argument("usuário"),
        Argument("id do discord")
    )

    override fun getParent() = AccountChangeCommand()

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        val discordId = args[1].toLong()

        targetUser.discordId = discordId

        CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().update(
            UpdateUserByIdDTO(
                targetUser.id
            ) {
                it.discordId = discordId
            }
        )

        commandSender.sendMessage(TextComponent("§eId do discord do usuário ${targetUser.name} atualizado!"))
        return true
    }

}