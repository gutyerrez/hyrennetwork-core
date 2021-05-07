package net.hyren.core.shared.users.preferences.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersPreferencesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<EntityID<UUID>, Array<Preference>> {
            CoreProvider.Repositories.Postgres.USERS_PREFERENCES_REPOSITORY.provide().fetchByUserId(
                FetchUserPreferencesByUserIdDTO(
                    it
                )
            )
        }

    fun fetchByUserId(userId: EntityID<UUID>) = this.CACHE.get(userId)

    fun put(
        userId: EntityID<UUID>,
        preferences: Array<Preference>
    ) = this.CACHE.put(userId, preferences)

}