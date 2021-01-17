package com.redefantasy.core.shared.users.punishments.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import com.redefantasy.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersPunishmentsLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build<UUID, List<UserPunishment>> {
                CoreProvider.Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY.provide().fetchByUserId(
                        FetchUserPunishmentsByUserIdDTO(
                                it
                        )
                )
            }

    fun fetchByUserId(userId: UUID) = this.CACHE.get(userId)

}