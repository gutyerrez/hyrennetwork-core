package com.redefantasy.core.spigot.command

import com.redefantasy.core.shared.commands.Commandable
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
abstract class CustomCommand(
    name: String
) : Command(name), Commandable<CommandSender> {

    override fun getSenderName(commandSender: CommandSender): String = commandSender.name
    
    override fun getDescription0(): String = this.getDescription()

    override fun getUsage(): Array<BaseComponent> {
        return super.getUsage0()
    }

    override fun getAliases0() = this.aliases.toTypedArray()

    override fun isPlayer(commandSender: CommandSender) = commandSender is Player

    override fun isConsole(commandSender: CommandSender) = commandSender is ConsoleCommandSender

    override fun execute(commandSender: CommandSender, label: String, args: Array<out String>): Boolean {
        executeRaw(commandSender, args)

        return false
    }

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean? = null

}