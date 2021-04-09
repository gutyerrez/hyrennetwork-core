package com.redefantasy.core.shared.users.skins.storage.repositories.implementations

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dao.UserSkinDAO
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinsByUserIdDTO
import com.redefantasy.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import com.redefantasy.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

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
				UsersSkinsTable.name eq fetchUserSkinByNameDTO.name
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
					it.updatedAt = DateTime.now(
						CoreConstants.DATE_TIME_ZONE
					)
				}
			}

			UserSkinDAO.new {
				this.name = name
				this.userId = userId
				this.signature = skin.signature
				this.value = skin.value
				this.enabled = enabled
				this.updatedAt = updatedAt
			}
		}
	}

}