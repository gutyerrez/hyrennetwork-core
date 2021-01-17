package com.redefantasy.core.shared.users.preferences.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.preferences.data.UserPreference
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersPreferencesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build<UUID, List<UserPreference>> {
                CoreProvider.Repositories.Mongo.USERS_PREFERENCES_REPOSITORY.provide().fetchByUserId(
                        FetchUserPreferencesByUserIdDTO(
                                it
                        )
                )
            }

    fun fetchByUserId(userId: UUID) = this.CACHE.get(userId)

}