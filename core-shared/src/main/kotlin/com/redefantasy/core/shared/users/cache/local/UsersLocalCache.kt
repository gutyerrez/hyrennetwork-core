package com.redefantasy.core.shared.users.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.dto.FetchUserByDiscordIdDTO
import com.redefantasy.core.shared.users.storage.dto.FetchUserByIdDTO
import com.redefantasy.core.shared.users.storage.dto.FetchUserByLastAddressDTO
import com.redefantasy.core.shared.users.storage.dto.FetchUserByNameDTO
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersLocalCache : LocalCache {

	private val CACHE_BY_ID = Caffeine.newBuilder()
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build<EntityID<UUID>, User?> {
			CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().fetchById(
				FetchUserByIdDTO(it)
			)
		}

	private val CACHE_BY_NAME = Caffeine.newBuilder()
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build<String, User?> {
			CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().fetchByName(
				FetchUserByNameDTO(it)
			)
		}

	private val CACHE_BY_DISCORD_ID = Caffeine.newBuilder()
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build<Long, User?> {
			CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().fetchByDiscordId(
				FetchUserByDiscordIdDTO(it)
			)
		}

	private val CACHE_BY_LAST_ADDRESS = Caffeine.newBuilder()
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build<String, List<User>> {
			CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().fetchByLastAddress(
				FetchUserByLastAddressDTO(it)
			)
		}

	fun fetchById(id: EntityID<UUID>) = this.CACHE_BY_ID.get(id)

	fun fetchById(id: UUID) = this.CACHE_BY_ID.get(
		EntityID(id, UsersTable)
	)

	fun fetchByName(name: String) = this.CACHE_BY_NAME.get(name)

	fun fetchByDiscordId(discordId: Long) = this.CACHE_BY_DISCORD_ID.get(discordId)

	fun fetchByAddress(lastAddress: String) = this.CACHE_BY_LAST_ADDRESS.get(lastAddress)

}