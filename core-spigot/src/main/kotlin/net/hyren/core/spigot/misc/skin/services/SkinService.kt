package net.hyren.core.spigot.misc.skin.services

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.misc.skin.controller.SkinController
import net.hyren.core.shared.misc.utils.Patterns
import net.hyren.core.shared.users.data.User
import net.hyren.core.shared.users.skins.data.UserSkin
import net.hyren.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import net.hyren.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import net.hyren.core.shared.users.skins.storage.dto.FetchUserSkinByUserIdAndNameDTO
import net.hyren.core.shared.users.skins.storage.dto.UpdateUserSkinDTO
import net.hyren.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
object SkinService {

	const val CHANGE_COOLDOWN = 15

	fun changeSkin(
		user: User,
		name: String
	): CommonResponse {
		if (!user.canChangeSkin()) {
			return CommonResponse.WAIT_FOR_CHANGE_SKIN_AGAIN
		}

		if (!Patterns.NICK.matches(name)) {
			return CommonResponse.INVALID_NICKNAME
		}

		val userSkin = CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByName(name)
			?: CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().fetchByName(
				FetchUserSkinByNameDTO(
					EntityID(
						name,
						UsersSkinsTable
					)
				)
			)

		val skin = userSkin?.skin ?: SkinController.fetchSkinByName(name) ?: return CommonResponse.SKIN_NOT_FOUND

		if (userSkin != null && userSkin.userId == user.id) {
			CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().update(
				UpdateUserSkinDTO(
					userSkin
				)
			)
		} else if (userSkin != null && userSkin.userId != user.id) {
			CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().create(
				CreateUserSkinDTO(
					UserSkin(
						EntityID(
							name,
							UsersSkinsTable
						),
						user.id,
						skin,
						true,
						DateTime.now(
							CoreConstants.DATE_TIME_ZONE
						)
					)
				)
			)
		} else {
			CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().create(
				CreateUserSkinDTO(
					UserSkin(
						EntityID(
							name,
							UsersSkinsTable
						),
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

		CoreProvider.Cache.Local.USERS_SKINS.provide().invalidate(user.id)

		return CommonResponse.CHANGING_SKIN_TO
	}

	fun refresh(
		user: User
	): CommonResponse {
		if (!user.canChangeSkin()) return CommonResponse.WAIT_FOR_CHANGE_SKIN_AGAIN

		val skin = SkinController.fetchSkinByName(user.name)

		if (skin !== null) {
			val userSkin = CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().fetchByUserIdAndName(
				FetchUserSkinByUserIdAndNameDTO(
					user.id,
					EntityID(
						user.name,
						UsersSkinsTable
					),
				)
			)

			if (userSkin !== null) {
				CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().update(
					UpdateUserSkinDTO(
						userSkin
					)
				)
			} else {
				CoreProvider.Repositories.PostgreSQL.USERS_SKINS_REPOSITORY.provide().create(
					CreateUserSkinDTO(
						UserSkin(
							EntityID(
								user.name,
								UsersSkinsTable
							),
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
		} else {
			return CommonResponse.SKIN_NOT_FOUND
		}

		return CommonResponse.DOWNLOADING_FROM_MOJANG
	}

	private fun User.canChangeSkin(): Boolean {
		return CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByUserId(id)?.stream()
			?.filter {
				it.updatedAt + TimeUnit.MINUTES.toMillis(
					CHANGE_COOLDOWN.toLong()
				) > DateTime.now(
					CoreConstants.DATE_TIME_ZONE
				)
			}
			?.findFirst()
			?.isPresent == false
	}

	enum class CommonResponse(
		val message: String = ""
	) {

		WAIT_FOR_CHANGE_SKIN_AGAIN(
			"§cAguarde %s para atualizar sua pele novamente."
		),
		INVALID_NICKNAME(
			"§cO nome inserido é inválido."
		),
		SKIN_NOT_FOUND(
			"§cNão foi possível localizar a pele."
		),
		CHANGING_SKIN_TO(
			"§aAlterando sua pele para a de %s..."
		),
		DOWNLOADING_FROM_MOJANG(
			"§aBaixando sua pele utilizada em sua conta da Mojang..."
		);

	}

}