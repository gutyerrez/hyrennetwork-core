package com.redefantasy.core.bungee.command.defaults.player.ignore.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.ignore.IgnoreCommand
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.ignored.data.IgnoredUser
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class IgnoreAddCommand : CustomCommand("add") {

    override fun getDescription() = "Ignora um usuário em específico."

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

        CoreProvider.Repositories.Mongo.USERS_IGNORED_REPOSITORY.provide().create(
            CreateIgnoredUserDTO(
                IgnoredUser(
                    user!!.id,
                    targetUser.id,
                    DateTime.now(
                        CoreConstants.DATE_TIME_ZONE
                    )
                )
            )
        )

        commandSender.sendMessage(TextComponent("§aUsuário ignorado com sucesso."))
        return true
    }

}