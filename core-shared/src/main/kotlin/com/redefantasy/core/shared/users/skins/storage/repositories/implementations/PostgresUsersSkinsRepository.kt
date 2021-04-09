package com.redefantasy.core.shared.users.skins.storage.repositories.implementations

import com.redefantasy.core.shared.misc.exposed.ilike
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dao.UserSkinDAO
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinsByUserIdDTO
import com.redefantasy.core.shared.users.skins.storage.dto.UpdateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import com.redefantasy.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresUsersSkinsRepository : IUsersSkinsRepository {

	override fun fetchByUserId(
		fetchUserSkinsByUserIdDTO: FetchUserSkinsByUserIdDTO
	): List<UserSkin> {
		return transaction {
			return@transaction UserSkinDAO.find {
				UsersSkinsTable.userId eq fetchUserSkinsByUserIdDTO.userId
			}.map { it.toUserSkin() }.toCollection(mutableListOf())
		}
	}

	override fun fetchByName(
		fetchUserSkinByNameDTO: FetchUserSkinByNameDTO
	): UserSkin? {
		return transaction {
			val result = UserSkinDAO.find {
				UsersSkinsTable.name ilike fetchUserSkinByNameDTO.name
			}

			return@transaction if (result.empty()) null else result.first().toUserSkin()
		}
	}

	override fun create(
		createUserSkinDTO: CreateUserSkinDTO
	) {
		val (
			name,
			userId,
			skin,
			enabled,
			updatedAt
		) = createUserSkinDTO.userSkin

		transaction {
			UserSkinDAO.find {
				UsersSkinsTable.userId eq userId
			}.forEach {
				if (it.enabled) {
					it.enabled = false
				}
			}

			UserSkinDAO.new {
				this.name = name
				this.userId = userId
				this.value = skin.value
				this.signature = skin.signature
				this.enabled = enabled
				this.updatedAt = updatedAt
			}
		}
	}

	override fun update(
		updateUserSkinDTO: UpdateUserSkinDTO
	) {
		transaction {
			val (
				_,
				userId,
				skin,
				enabled,
				updatedAt
			) = updateUserSkinDTO.userSkin

			transaction {
				val result = UserSkinDAO.find {
					UsersSkinsTable.userId eq userId and (
						UsersSkinsTable.value eq skin.value
					) and (
						UsersSkinsTable.signature eq skin.signature
					)
				}

				if (!result.empty()) {
					val result = result.first()

					result.value = skin.value
					result.signature = skin.signature
					result.enabled = enabled
					result.updatedAt = updatedAt
				}
			}
		}
	}

}