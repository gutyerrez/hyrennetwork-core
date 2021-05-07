package net.hyren.core.shared.users.skins.storage.repositories.implementations

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.misc.exposed.ilike
import net.hyren.core.shared.users.skins.data.UserSkin
import net.hyren.core.shared.users.skins.storage.dao.UserSkinDAO
import net.hyren.core.shared.users.skins.storage.dto.*
import net.hyren.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import net.hyren.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.sql.and
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
			}.map { it.toUserSkin() }
		}
	}

	override fun fetchByName(
		fetchUserSkinByNameDTO: FetchUserSkinByNameDTO
	): UserSkin? {
		return transaction {
			UserSkinDAO.find {
				UsersSkinsTable.name ilike fetchUserSkinByNameDTO.name
			}.firstOrNull()?.toUserSkin()
		}
	}

	override fun fetchByUserIdAndName(
		fetchUserSkinByUserIdAndNameDTO: FetchUserSkinByUserIdAndNameDTO
	): UserSkin? {
		return transaction {
			UserSkinDAO.find {
				UsersSkinsTable.userId eq fetchUserSkinByUserIdAndNameDTO.userId and (
					UsersSkinsTable.name ilike fetchUserSkinByUserIdAndNameDTO.name
				)
			}.firstOrNull()?.toUserSkin()
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
				it.enabled = false
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
				name,
				userId,
				skin
			) = updateUserSkinDTO.userSkin

			transaction {
				UserSkinDAO.find {
					UsersSkinsTable.userId eq userId
				}.forEach {
					it.enabled = false
				}

				UserSkinDAO.find {
					UsersSkinsTable.userId eq userId and (
						UsersSkinsTable.name eq name
					)
				}.firstOrNull().apply {
					this?.value = skin.value
					this?.signature = skin.signature
					this?.enabled = true
					this?.updatedAt = DateTime.now(
						CoreConstants.DATE_TIME_ZONE
					)
				}
			}
		}
	}

}