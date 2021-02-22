package com.redefantasy.core.bungee.command.defaults.staff.account.subcommands

import com.redefantasy.core.bungee.command.CustomCommand

/**
 * @author Gutyerrez
 */
class AccountChangeCommand : CustomCommand("change") {

    override fun getSubCommands() = listOf(
        AccountChangePasswordCommand(),
        AccountChangeEmailCommand(),
        AccountChangeDiscordIdCommand()
    )

}