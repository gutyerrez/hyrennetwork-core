package net.hyren.core.shared.users.skins.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.users.skins.data.UserSkin
import net.hyren.core.shared.users.skins.storage.dto.FetchUserSkinsByUserIdDTO
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class UsersSkinsLocalCache : LocalCache {

	private val CACHE = Caffeine.newBuilder()
		.expireAfterWrite(3, TimeUnit.MINUTES)
		.build<EntityID<UUID>, List<UserSkin>> {
			CoreProvider.Repositories.MariaDB.USERS_SKINS_REPOSITORY.provide().fetchByUserId(
				FetchUserSkinsByUserIdDTO(
					it
				)
			)
		}

	fun fetchByUserId(userId: EntityID<UUID>) = this.CACHE.get(userId)

	fun fetchByUserId(userId: UUID) = this.CACHE.get(
		EntityID(
			userId,
			UsersTable
		)
	)

	fun fetchByName(name: String): UserSkin? = this.CACHE.asMap().values.stream()
		.map {
			it.stream().filter { userSkin -> userSkin.name == name }.findFirst()
		}.findFirst().orElse(null)?.orElse(null)

	fun invalidate(userId: EntityID<UUID>) = this.CACHE.invalidate(userId)

}