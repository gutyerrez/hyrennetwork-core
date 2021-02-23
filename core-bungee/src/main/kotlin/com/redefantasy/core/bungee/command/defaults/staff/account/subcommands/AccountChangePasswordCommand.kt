package com.redefantasy.core.bungee.command.defaults.staff.account.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.misc.utils.EncryptionUtil
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class AccountChangePasswordCommand : CustomCommand("senha") {

    override fun getDescription() = "Redefinir a senha de um usuário."

    override fun getArguments() = listOf(
        Argument("usuário"),
        Argument("nova senha")
    )

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        val password = EncryptionUtil.hash(
            EncryptionUtil.Type.SHA256,
            args[1]
        )

        CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().create(
            CreateUserPasswordDTO(
                targetUser.getUniqueId(),
                password
            )
        )

        commandSender.sendMessage(TextComponent("§eSenha do usuário ${targetUser.name} atualizada!"))
        return true
    }

}