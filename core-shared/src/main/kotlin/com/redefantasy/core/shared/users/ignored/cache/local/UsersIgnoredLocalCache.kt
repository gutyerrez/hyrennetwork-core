package com.redefantasy.core.shared.users.ignored.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.ignored.data.UserIgnored
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchUsersIgnoredByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersIgnoredLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(7, TimeUnit.MINUTES)
            .build<UUID, List<UserIgnored>> {
                CoreProvider.Repositories.Mongo.USERS_IGNORED_REPOSITORY.provide().fetchByUserId(
                        FetchUsersIgnoredByUserIdDTO(
                                it
                        )
                )
            }

    fun fetchByUserId(userId: UUID) = this.CACHE.get(userId)

}