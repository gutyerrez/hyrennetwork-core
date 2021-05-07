package net.hyren.core.spigot.misc.skin.command

import net.hyren.core.shared.commands.restriction.CommandRestriction
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.core.spigot.misc.player.sendNonSuccessResponse
import net.hyren.core.spigot.misc.skin.command.subcommands.SkinHelpCommand
import net.hyren.core.spigot.misc.skin.command.subcommands.SkinRefreshCommand
import net.hyren.core.spigot.misc.skin.inventory.SkinsInventory
import net.hyren.core.spigot.misc.skin.services.SkinService
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinCommand : CustomCommand("skin") {

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
					player.sendNonSuccessResponse(response)
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

}