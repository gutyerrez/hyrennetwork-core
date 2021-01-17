package com.redefantasy.core.shared.users.friends.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.friends.data.UserFriend
import com.redefantasy.core.shared.users.friends.storage.dto.CreateUserFriendDTO
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteUserFriendDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchUsersFriendsByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.repositories.IUsersFriendsRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersFriendsRepository(
        databaseProvider: MongoDatabaseProvider
) : MongoRepository<UserFriend>(
        databaseProvider,
        "users_friends",
        UserFriend::class
), IUsersFriendsRepository {

    override fun fetchByUserId(
        fetchUsersFriendsByUserIdDTO: FetchUsersFriendsByUserIdDTO
    ): List<UserFriend> {
        val userFriends = mutableListOf<UserFriend>()

        userFriends.addAll(
                this.mongoCollection.find(
                        Filters.eq("user_id", fetchUsersFriendsByUserIdDTO.userId)
                )
        )

        return userFriends
    }

    override fun create(createUserFriendDTO: CreateUserFriendDTO) {
        this.mongoCollection.insertOne(
                createUserFriendDTO.userFriend
        )
    }

    override fun delete(deleteUserFriendDTO: DeleteUserFriendDTO) {
        this.mongoCollection.deleteOne(
                Filters.eq(deleteUserFriendDTO.userFriend)
        )
    }

}