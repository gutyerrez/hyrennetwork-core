package com.redefantasy.core.shared.users.friends.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.friends.data.FriendUser
import com.redefantasy.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.repositories.IUsersFriendsRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersFriendsRepository(
    databaseProvider: MongoDatabaseProvider
) : MongoRepository<FriendUser>(
    databaseProvider,
    "users_friends",
    FriendUser::class
), IUsersFriendsRepository {

    override fun fetchByUserId(
        fetchFriendUsersByUserIdDTO: FetchFriendUsersByUserIdDTO
    ): List<FriendUser> {
        val userFriends = mutableListOf<FriendUser>()

        userFriends.addAll(
            this.mongoCollection.find(
                Filters.eq("user_id", fetchFriendUsersByUserIdDTO.userId)
            )
        )

        return userFriends
    }

    override fun fetchFriendRequestsByUserId(
        fetchFriendRequestsByUserId: FetchFriendRequestsByUserIdDTO
    ): List<FriendUser> {
        val userFriends = mutableListOf<FriendUser>()

        userFriends.addAll(
            this.mongoCollection.find(
                Filters.eq("friend_user_id", fetchFriendRequestsByUserId.userId)
            )
        )

        return userFriends
    }

    override fun create(createFriendUserDTO: CreateFriendUserDTO) {
        this.mongoCollection.insertOne(
            createFriendUserDTO.friendUser
        )
    }

    override fun delete(deleteFriendUserDTO: DeleteFriendUserDTO) {
        this.mongoCollection.deleteOne(
            Filters.and(
                Filters.eq("user_id", deleteFriendUserDTO.userId),
                Filters.eq("friend_id", deleteFriendUserDTO.friendId)
            )
        )
    }

}