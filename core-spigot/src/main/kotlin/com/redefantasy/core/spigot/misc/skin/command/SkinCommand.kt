package com.redefantasy.core.spigot.misc.skin.command

import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.skin.command.subcommands.SkinHelpCommand
import com.redefantasy.core.spigot.misc.skin.command.subcommands.SkinRefreshCommand
import com.redefantasy.core.spigot.misc.skin.inventory.SkinsInventory
import com.redefantasy.core.spigot.misc.skin.services.SkinService
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinCommand : CustomCommand("skin"), GroupCommandRestrictable {

	override fun getCommandRestriction() = CommandRestriction.GAME

	override fun getSubCommands() = listOf(
		SkinRefreshCommand(),
		SkinHelpCommand()
	)

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		val player = commandSender as Player

		when (args.size) {
			1 -> {
				val name = args[0]

				if (name == "cancelar") {
					player.closeInventory()
					return false
				}

				val response = SkinService.changeSkin(
					user!!,
					name
				)

				if (response != SkinService.CommonResponse.CHANGING_SKIN_TO) {
					player.sendMessage(
						TextComponent(
							response.message
						)
					)
					return false
				}

				player.sendMessage(
					TextComponent(
						String.format(
							response.message,
							name
						)
					)
				)

				commandSender.sendMessage(
					TextComponent("§aSua pele foi alterada com sucesso, relogue para que ela atualize.")
				)
			}
			else -> player.openInventory(SkinsInventory(user!!))
 		}

		return true
	}

	override fun getGroup() = Group.MVP

}