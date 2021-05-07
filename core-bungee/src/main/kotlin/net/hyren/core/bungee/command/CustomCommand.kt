package net.hyren.core.bungee.command

import net.hyren.core.shared.commands.Commandable
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import net.md_5.bungee.command.ConsoleCommandSender

/**
 * @author Gutyerrez
 */
abstract class CustomCommand(
    name: String
) : Command(name), Commandable<CommandSender>, TabExecutor {

    open fun getDescription(): String? = null

    override fun getDescription0() = getDescription()

    open fun getUsage() = this.getUsage0()

    override fun getSenderName(
        commandSender: CommandSender
    ): String = commandSender.name

    override fun isPlayer(
        commandSender: CommandSender
    ) = commandSender is ProxiedPlayer

    override fun isConsole(
        commandSender: CommandSender
    ) = commandSender is ConsoleCommandSender

    override fun execute(
        commandSender: CommandSender,
        args: Array<out String>
    ) {
        this.executeRaw(
            commandSender,
            args
        )
    }

    override fun getAliases0(): Array<String> = this.aliases

    override fun onTabComplete(
        commandSender: CommandSender,
        args: Array<out String>
    ) = this.onTabComplete0(
        commandSender,
        args
    )

}