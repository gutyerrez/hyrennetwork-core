package com.redefantasy.core.shared.users.friends.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.friends.data.UserFriend
import com.redefantasy.core.shared.users.friends.storage.dto.FetchUsersFriendsByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersFriendsLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(7, TimeUnit.MINUTES)
            .build<UUID, List<UserFriend>> {
                CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().fetchByUserId(
                        FetchUsersFriendsByUserIdDTO(
                                it
                        )
                )
            }

    fun fetchByUserId(userId: UUID) = this.CACHE.get(userId)

}