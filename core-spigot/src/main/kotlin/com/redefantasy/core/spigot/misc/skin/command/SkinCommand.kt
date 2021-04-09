package com.redefantasy.core.spigot.misc.skin.command

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.skin.controller.SkinController
import com.redefantasy.core.shared.misc.utils.Patterns
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.UpdateUserSkinDTO
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.skin.command.subcommands.SkinRefreshCommand
import com.redefantasy.core.spigot.misc.skin.inventory.SkinsInventory
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class SkinCommand : CustomCommand("skin"), GroupCommandRestrictable {

	override fun getCommandRestriction() = CommandRestriction.GAME

	override fun getSubCommands() = listOf(
		SkinRefreshCommand()
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

				if (!Patterns.NICK.matches(name)) {
					commandSender.sendMessage(
						TextComponent("§cO nome inserido é inválido.")
					)
					return false
				}

				var userSkin = CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByName(name) ?: CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().fetchByName(
					FetchUserSkinByNameDTO(name)
				)

				if (userSkin !== null && userSkin.userId == user!!.id) {
					userSkin = UserSkin(
						name,
						user.id,
						userSkin.skin,
						userSkin.enabled,
						DateTime.now(
							CoreConstants.DATE_TIME_ZONE
						)
					)
				}

				val skin = userSkin?.skin ?: SkinController.fetchSkinByName(name)

				if (skin === null) {
					commandSender.sendMessage(
						TextComponent("§cNão foi possível localizar a pele.")
					)
					return false
				}

				commandSender.sendMessage(
					TextComponent("§aAlterando sua skin para a de $name...")
				)

				if (userSkin !== null && userSkin.userId == user!!.id) {
					CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().update(
						UpdateUserSkinDTO(
							userSkin
						)
					)
				} else {
					CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().create(
						CreateUserSkinDTO(
							UserSkin(
								name,
								user!!.id,
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

				CoreProvider.Cache.Local.USERS_SKINS.provide().invalidate(user.id)
			}
			else -> player.openInventory(SkinsInventory(user!!))
 		}

		return true
	}

	override fun getGroup() = Group.MVP

}