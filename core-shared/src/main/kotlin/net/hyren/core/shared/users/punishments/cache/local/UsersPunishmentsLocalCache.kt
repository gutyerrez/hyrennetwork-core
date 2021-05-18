package net.hyren.core.shared.users.punishments.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.users.punishments.data.UserPunishment
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentByIdDTO
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersPunishmentsLocalCache : LocalCache {

    private val CACHE_BY_ID = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build<EntityID<Int>, UserPunishment> {
                CoreProvider.Repositories.PostgreSQL.USERS_PUNISHMENTS_REPOSITORY.provide().fetchById(
                        FetchUserPunishmentByIdDTO(
                                it
                        )
                )
            }

    private val CACHE_BY_UUID = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build<EntityID<UUID>, List<UserPunishment>> {
                CoreProvider.Repositories.PostgreSQL.USERS_PUNISHMENTS_REPOSITORY.provide().fetchByUserId(
                        FetchUserPunishmentsByUserIdDTO(
                                it
                        )
                )
            }

    fun fetchById(id: EntityID<Int>) = this.CACHE_BY_ID.get(id)

    fun fetchByUserId(userId: EntityID<UUID>) = this.CACHE_BY_UUID.get(userId)

    fun invalidate(userId: UUID) = this.CACHE_BY_UUID.invalidate(
            EntityID(userId, UsersTable)
    )

}