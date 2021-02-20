package com.redefantasy.core.bungee.command.defaults.staff.account.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

/**
 * @author Gutyerrez
 */
class AccountChangeCommand : CustomCommand("change") {

    override fun getSubCommands() = listOf(
        AccountChangePasswordCommand(),
        AccountChangeEmailCommand(),
        AccountChangeDiscordIdCommand()
    )

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        TODO("Not yet implemented")
    }

}