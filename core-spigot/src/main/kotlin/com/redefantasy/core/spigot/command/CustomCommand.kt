package com.redefantasy.core.spigot.command

import com.redefantasy.core.shared.commands.Commandable
import com.redefantasy.core.shared.users.data.User
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
abstract class CustomCommand(
    name: String
) : Command(name), Commandable<CommandSender>, TabCompleter {

    override fun getSenderName(
        commandSender: CommandSender
    ): String = commandSender.name
    
    override fun getDescription0(): String = this.getDescription()

    override fun getUsage() = super.getUsage0()

    override fun getAliases0(): Array<String> = this.aliases.map { it.toString() }.toTypedArray()

    override fun isPlayer(
        commandSender: CommandSender
    ) = commandSender is Player

    override fun isConsole(
        commandSender: CommandSender
    ) = commandSender is ConsoleCommandSender

    override fun execute(
        commandSender: CommandSender,
        label: String,
        args: Array<out String>
    ): Boolean {
        executeRaw(commandSender, args)

        return false
    }

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean? = null

    fun sendAvailableCommands(
        commandSender: CommandSender,
        args: Array<out String>
    ): Boolean {
        Commandable::class.java.declaredMethods.forEach {
            if (it.name == "sendAvailableCommands0") {
                it.parameters.forEach { parameter ->
                    println(parameter::class.java)
                }
            }
        }

        return true
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        _command: Command,
        _label: String,
        args: Array<out String>
    ): MutableList<String?> = this.onTabComplete0(
        commandSender,
        args
    ).toMutableList()

}