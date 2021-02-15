package com.redefantasy.core.bungee.misc.login.commands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Gutyerrez
 */
class LoginCommand : CustomCommand("logar") {

    override fun getDescription() = "Autenticar sua conta."

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getArguments() = listOf(
        Argument("senha")
    )

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        if (user === null) {
            commandSender.sendMessage(TextComponent("§cVocê não está registrado."))
            return false
        }

        val currentPassword = CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().fetchByUserId(
            FetchUserPasswordByUserIdDTO(user.getUniqueId())
        ).stream()
            .filter { it.enabled }
            .findFirst()
            .orElse(null)

        if (currentPassword === null) {
            commandSender.sendMessage(TextComponent("§cVocê não está registrado."))
            return false
        }

        val successfully = user.attemptLogin(args[0])

        if (!successfully && user.loginAttempts.size >= 3) {
            commandSender as ProxiedPlayer

            commandSender.disconnect(TextComponent("§cVocê errou sua senha várias vezes."))
            return false
        }

        user.setLogged(successfully)
        commandSender.sendMessage("§aVocê logou com sucesso!")
        return true
    }

}