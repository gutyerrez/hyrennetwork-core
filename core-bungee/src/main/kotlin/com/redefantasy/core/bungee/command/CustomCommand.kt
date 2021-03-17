package com.redefantasy.core.bungee.command

import com.redefantasy.core.shared.commands.Commandable
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.command.ConsoleCommandSender

/**
 * @author Gutyerrez
 */
abstract class CustomCommand(
    name: String
) : Command(name), Commandable<CommandSender> {

    fun getDescription(): String? = this.getDescription0()

    override fun getSenderName(commandSender: CommandSender): String = commandSender.name

    override fun isPlayer(commandSender: CommandSender) = commandSender is ProxiedPlayer

    override fun isConsole(commandSender: CommandSender) = commandSender is ConsoleCommandSender

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        this.executeRaw(
            commandSender,
            args
        )
    }

    override fun getAliases0(): Array<String> = this.aliases

}