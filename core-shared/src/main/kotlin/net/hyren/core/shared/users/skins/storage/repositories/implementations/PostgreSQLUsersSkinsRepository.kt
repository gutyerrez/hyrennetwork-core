package net.hyren.core.shared.users.skins.storage.repositories.implementations

import net.hyren.core.shared.*
import net.hyren.core.shared.misc.exposed.ilike
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
class PostgreSQLUsersSkinsRepository : IUsersSkinsRepository {

	override fun fetchByUserId(
		fetchUserSkinsByUserIdDTO: FetchUserSkinsByUserIdDTO
	) = transaction(
		CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
	) {
		UserSkinDAO.find {
			UsersSkinsTable.userId eq fetchUserSkinsByUserIdDTO.userId
		}.map { it.toUserSkin() }
	}

	override fun fetchByName(
		fetchUserSkinByNameDTO: FetchUserSkinByNameDTO
	) = transaction(
		CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
	) {
		UserSkinDAO.find {
			UsersSkinsTable.name ilike fetchUserSkinByNameDTO.name
		}.firstOrNull()?.toUserSkin()
	}

	override fun fetchByUserIdAndName(
		fetchUserSkinByUserIdAndNameDTO: FetchUserSkinByUserIdAndNameDTO
	) = transaction(
		CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
	) {
		UserSkinDAO.find {
			UsersSkinsTable.userId eq fetchUserSkinByUserIdAndNameDTO.userId and (
				UsersSkinsTable.name ilike fetchUserSkinByUserIdAndNameDTO.name
			)
		}.firstOrNull()?.toUserSkin()
	}

	override fun create(
		createUserSkinDTO: CreateUserSkinDTO
	) = transaction(
		CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
	) {
		UserSkinDAO.find {
			UsersSkinsTable.userId eq createUserSkinDTO.userSkin.userId
		}.forEach { it.enabled = false }

		UserSkinDAO.new {
			this.name = createUserSkinDTO.userSkin.name
			this.userId = createUserSkinDTO.userSkin.userId
			this.value = createUserSkinDTO.userSkin.skin.value
			this.signature = createUserSkinDTO.userSkin.skin.signature
			this.enabled = createUserSkinDTO.userSkin.enabled
			this.updatedAt = createUserSkinDTO.userSkin.updatedAt
		}.toUserSkin()
	}

	override fun update(
		updateUserSkinDTO: UpdateUserSkinDTO
	) = transaction(
		CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
	) {
		val (
			name,
			userId,
			skin
		) = updateUserSkinDTO.userSkin

		UserSkinDAO.find {
			UsersSkinsTable.userId eq userId
		}.forEach {
			if (it.name == name) {
				it.value = skin.value
				it.signature = skin.signature
				it.enabled = true
				it.updatedAt = DateTime.now(
					CoreConstants.DATE_TIME_ZONE
				)
			} else it.enabled = false
		}
	}

}