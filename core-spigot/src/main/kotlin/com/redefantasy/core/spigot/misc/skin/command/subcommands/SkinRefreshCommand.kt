package com.redefantasy.core.spigot.misc.skin.command.subcommands

import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.player.sendNonSuccessResponse
import com.redefantasy.core.spigot.misc.skin.command.SkinCommand
import com.redefantasy.core.spigot.misc.skin.services.SkinService
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinRefreshCommand : CustomCommand("atualizar") {

	override fun getParent() = SkinCommand()

	override fun getDescription() = "Atualizar sua pele com a da Mojang."

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		commandSender as Player

		val response = SkinService.refresh(user!!)

		if (response != SkinService.CommonResponse.DOWNLOADING_FROM_MOJANG) {
			commandSender.sendNonSuccessResponse(response)
			return false
		}

		commandSender.sendMessage(
			arrayOf(
				TextComponent(response.message),
				TextComponent("§aSua pele foi alterada com sucesso, relogue para que ela atualize.")
			)
		)
		return true
	}

}