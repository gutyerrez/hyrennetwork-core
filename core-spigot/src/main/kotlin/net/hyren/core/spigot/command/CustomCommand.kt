package net.hyren.core.spigot.command

import net.hyren.core.shared.commands.Commandable
import net.hyren.core.shared.users.data.User
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

    override fun getUsage() = this.getUsage0()

    override fun getAliases() = getAliases0().toMutableList()

    override fun getAliases0() = emptyArray<String>()

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