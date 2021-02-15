package com.redefantasy.core.bungee.misc.login.commands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.misc.utils.EncryptionUtil
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import com.redefantasy.core.shared.users.storage.dto.CreateUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.net.InetAddress
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
class RegisterCommand : CustomCommand("registrar") {

    override fun getDescription() = "Registrar uma conta."

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getArguments(): List<Argument> = listOf(
        Argument("senha"),
        Argument("confirme a senha")
    )

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        if (user !== null && CoreProvider.Cache.Local.USERS_PASSWORDS.provide().fetchById(user.getUniqueId()).isNotEmpty()) {
            commandSender.sendMessage(TextComponent("§cVocê já está registrado."))
            return false
        }

        commandSender as ProxiedPlayer

        var _user: User? = user

        if (_user === null) {
            _user = CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().create(
                CreateUserDTO(
                    commandSender.uniqueId,
                    commandSender.name,
                    (commandSender.socketAddress as InetSocketAddress).address.hostAddress
                )
            )
        }

        if (args[0] !== args[1]) {
            commandSender.sendMessage(TextComponent("§cAs senhas não coincidem."))
            return false
        }

        val currentPasswords = CoreProvider.Cache.Local.USERS_PASSWORDS.provide().fetchById(_user.getUniqueId())

        if (currentPasswords.isNotEmpty() && currentPasswords.stream().anyMatch {
                it.password === EncryptionUtil.hash(EncryptionUtil.Type.SHA256, args[0])
        }) {
            commandSender.sendMessage(TextComponent("§cVocê já usou essa senha anteriormente."))
            return false
        }

        CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().create(
            CreateUserPasswordDTO(
                _user.getUniqueId(),
                args[0]
            )
        )

        _user.setLogged(true)

        commandSender.sendMessage(TextComponent("§eRegistrado com sucesso."))
        return false
    }

}