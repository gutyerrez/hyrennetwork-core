package com.redefantasy.core.shared.users.preferences.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.misc.preferences.data.Preference
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersPreferencesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<EntityID<UUID>, Array<Preference<*>>> {
            CoreProvider.Repositories.Postgres.USERS_PREFERENCES_REPOSITORY.provide().fetchByUserId(
                FetchUserPreferencesByUserIdDTO(
                    it
                )
            )
        }

    fun fetchByUserId(userId: EntityID<UUID>) = this.CACHE.get(userId)

}