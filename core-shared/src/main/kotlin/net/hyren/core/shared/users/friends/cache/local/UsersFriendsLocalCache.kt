package net.hyren.core.shared.users.friends.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.users.friends.data.FriendUser
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersFriendsLocalCache : LocalCache {

    private val FRIENDS_CACHE = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<EntityID<UUID>, List<FriendUser>> {
            CoreProvider.Repositories.MariaDB.USERS_FRIENDS_REPOSITORY.provide().fetchByUserId(
                FetchFriendUsersByUserIdDTO(
                    it
                )
            )
        }

    private val FRIEND_REQUESTS_CACHE = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<EntityID<UUID>, List<FriendUser>> {
            CoreProvider.Repositories.MariaDB.USERS_FRIENDS_REPOSITORY.provide().fetchFriendRequestsByUserId(
                FetchFriendRequestsByUserIdDTO(
                    it
                )
            )
        }

    fun fetchByUserId(userId: EntityID<UUID>) = this.FRIENDS_CACHE.get(userId)

    fun fetchByUserId(userId: UUID) = this.FRIENDS_CACHE.get(
        EntityID(userId, UsersTable)
    )

    fun fetchFriendRequestsByUserId(
        userId: EntityID<UUID>
    ) = this.FRIEND_REQUESTS_CACHE.get(userId)

    fun fetchFriendRequestsByUserId(userId: UUID) = this.FRIEND_REQUESTS_CACHE.get(
        EntityID(userId, UsersTable)
    )

    fun invalidate(userId: EntityID<UUID>) {
        this.FRIENDS_CACHE.invalidate(userId)
        this.FRIEND_REQUESTS_CACHE.invalidate(userId)
    }

}