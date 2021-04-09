package com.redefantasy.core.spigot.misc.skin.command.subcommands

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.skin.controller.SkinController
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.skin.command.SkinCommand
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class SkinRefreshCommand : CustomCommand("atualizar") {

	override fun getParent() = SkinCommand()

	override fun onCommand(
		commandSender: CommandSender,
		user: User?,
		args: Array<out String>
	): Boolean {
		commandSender.sendMessage(
			TextComponent("§aBaixando sua pele utilizada em sua conta da Mojang...")
		)

		val skin = SkinController.fetchSkinByName(user!!.name)

		if (skin !== null) {
			CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().create(
				CreateUserSkinDTO(
					UserSkin(
						user.name,
						user.id,
						skin,
						true,
						DateTime.now(
							CoreConstants.DATE_TIME_ZONE
						)
					)
				)
			)
		}

		commandSender.sendMessage(
			TextComponent("§aSua pele foi alterada com sucesso, relogue para que ela atualize.")
		)
		return true
	}

}