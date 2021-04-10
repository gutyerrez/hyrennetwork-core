package com.redefantasy.core.spigot.misc.skin.services

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.skin.controller.SkinController
import com.redefantasy.core.shared.misc.utils.Patterns
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinBySkinValueAndSignatureDTO
import com.redefantasy.core.shared.users.skins.storage.dto.UpdateUserSkinDTO
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
object SkinService {

	const val CHANGE_COOLDOWN = 15

	fun changeSkin(
		user: User,
		name: String
	): CommonResponse {
		if (!Patterns.NICK.matches(name)) return CommonResponse.INVALID_NICKNAME

		var userSkin = CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByName(name) ?: CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().fetchByName(
			FetchUserSkinByNameDTO(name)
		)

		if (userSkin !== null && userSkin.userId == user.id) {
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

		if (skin === null) return CommonResponse.SKIN_NOT_FOUND

		if (userSkin !== null && userSkin.userId == user.id) {
			CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().update(
				UpdateUserSkinDTO(
					userSkin
				)
			)
		} else if (userSkin !== null && userSkin.userId != user.id) {
			CreateUserSkinDTO(
				UserSkin(
					name,
					user.id,
					skin,
					true,
					DateTime.now(
						CoreConstants.DATE_TIME_ZONE
					)
				)
			)
		} else {
			CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().create(
				CreateUserSkinDTO(
					UserSkin(
						name,
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
		val skin = SkinController.fetchSkinByName(user.name)

		if (skin !== null) {
			val userSkin = CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().fetchBySkinValueAndSignature(
				FetchUserSkinBySkinValueAndSignatureDTO(
					skin.value,
					skin.signature
				)
			)

			if (userSkin !== null && userSkin.userId == user.id) {
				CoreProvider.Repositories.Postgres.USERS_SKINS_REPOSITORY.provide().update(
					UpdateUserSkinDTO(
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
			} else {
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
		} else return CommonResponse.SKIN_NOT_FOUND

		return CommonResponse.DOWNLOADING_FROM_MOJANG
	}

	enum class CommonResponse(
		val message: String = ""
	) {

		UNKNOWN,

		INVALID_NICKNAME(
			"§cO nome inserido é inválido."
		),
		SKIN_NOT_FOUND(
			"§cNão foi possível localizar a pele."
		),
		CHANGING_SKIN_TO(
			"§aAlterando sua skin para a de %s..."
		),
		DOWNLOADING_FROM_MOJANG(
			"§aBaixando sua pele utilizada em sua conta da Mojang..."
		);

	}

}