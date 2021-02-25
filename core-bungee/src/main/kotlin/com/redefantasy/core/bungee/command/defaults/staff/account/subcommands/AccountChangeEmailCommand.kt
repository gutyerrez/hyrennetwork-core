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
class AccountChangeEmailCommand : CustomCommand("email") {

    override fun getDescription() = "Alterar e-mail de um usuário."

    override fun getArguments() = listOf(
        Argument("usuário"),
        Argument("novo e-mail")
    )

    override fun getParent() = AccountChangeCommand()

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

        val email = args[1]

        targetUser.email = email

        CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().update(
            UpdateUserByIdDTO(
                targetUser.id
            ) {
                it.email = email
            }
        )

        commandSender.sendMessage(TextComponent("§eE-mail do usuário ${targetUser.name} atualizado!"))
        return true
    }

}