package com.redefantasy.core.spigot.misc.skin.command

import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.skin.inventory.SkinsInventory
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinCommand : CustomCommand("skin") {

	override fun getCommandRestriction() = CommandRestriction.GAME

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		val player = commandSender as Player

		player.openInventory(SkinsInventory())
		return true
	}

}