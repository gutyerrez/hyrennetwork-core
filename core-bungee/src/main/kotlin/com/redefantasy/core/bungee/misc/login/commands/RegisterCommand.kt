package com.redefantasy.core.bungee.misc.login.commands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.echo.packets.TitlePacket
import com.redefantasy.core.shared.misc.utils.EncryptionUtil
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import com.redefantasy.core.shared.users.storage.dto.CreateUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
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
        if (user !== null && CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().fetchByUserId(
                FetchUserPasswordByUserIdDTO(user.getUniqueId())
            ).isNotEmpty()
        ) {
            commandSender.sendMessage(TextComponent("§cVocê já está registrado. Utilize /logar <senha>."))
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

        if (!args[0].contentEquals(args[1])) {
            commandSender.sendMessage(TextComponent("§cAs senhas não coincidem. (${args[0]}|${args[1]})"))
            return false
        }

        val currentPasswords =
            CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().fetchByUserId(
                FetchUserPasswordByUserIdDTO(_user.getUniqueId())
            )

        if (currentPasswords.isNotEmpty() && currentPasswords.stream().anyMatch {
                it.password === EncryptionUtil.hash(EncryptionUtil.Type.SHA256, args[0])
            }) {
            commandSender.sendMessage(TextComponent("§cVocê já usou essa senha anteriormente."))
            return false
        }

        CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().create(
            CreateUserPasswordDTO(
                _user.getUniqueId(),
                EncryptionUtil.hash(EncryptionUtil.Type.SHA256, args[0])
            )
        )

        val users = CoreProvider.Cache.Local.USERS.provide().fetchByAddress(_user.lastAddress!!)

        if (users !== null && users.size > 1) {
            commandSender.sendMessage(TextComponent("§cVocê já atingiu o limite de cadastros."))
            return false
        }

        _user.setLogged(true)

        val packet = TitlePacket()

        packet.userId = _user.getUniqueId()
        packet.title = "§a§lRegistrado!"
        packet.subTitle = "§fRedirecionando..."

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
        return false
    }

}