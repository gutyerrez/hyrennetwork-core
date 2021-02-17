package com.redefantasy.core.spigot.command

import com.redefantasy.core.shared.commands.Commandable
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
abstract class CustomCommand(
    name: String
) : Command(name), Commandable<CommandSender> {

    override fun getDescription() = ""

    override fun getUsage(): Array<BaseComponent> {
        return super<Commandable>.getUsage()
    }

    override fun getAliases() = arrayOf<String>()

    override fun execute(commandSender: CommandSender, label: String, args: Array<out String>): Boolean {
        executeRaw(commandSender, args)

        return false
    }

    abstract override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean

}