package net.hyren.core.spigot.misc.skin.command.subcommands

import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.core.spigot.misc.skin.command.SkinCommand
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class SkinHelpCommand : CustomCommand("ajuda") {

	override fun getParent() = SkinCommand()

	override fun getDescription() = "Exibir esta mensagem."

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		return this.sendAvailableCommands(commandSender)
	}

}