package com.redefantasy.core.shared.users.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.dto.FetchUserByDiscordIdDTO
import com.redefantasy.core.shared.users.storage.dto.FetchUserByIdDTO
import com.redefantasy.core.shared.users.storage.dto.FetchUserByNameDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersLocalCache : LocalCache {

    private val CACHE_BY_ID = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build<UUID, User?> {
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

    fun fetchById(id: UUID) = this.CACHE_BY_ID.get(id)

    fun fetchByName(name: String) = this.CACHE_BY_NAME.get(name)

    fun fetchByDiscordId(discordId: Long) = this.CACHE_BY_DISCORD_ID.get(discordId)

}