package com.redefantasy.core.spigot.misc.skin.command.subcommands

import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.skin.command.SkinCommand
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class SkinHelpCommand : CustomCommand("ajuda") {

	override fun getParent() = SkinCommand()

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		return this.sendAvailableCommands<CommandSender>(commandSender, args)
	}

}