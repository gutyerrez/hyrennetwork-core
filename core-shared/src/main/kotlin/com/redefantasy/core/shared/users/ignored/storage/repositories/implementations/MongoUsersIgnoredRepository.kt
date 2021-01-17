package com.redefantasy.core.shared.users.ignored.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.ignored.data.UserIgnored
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateUserFriendDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteUserFriendDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchUsersIgnoredByUserIdDTO
import com.redefantasy.core.shared.users.ignored.storage.repositories.IUsersIgnoredRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersIgnoredRepository(
        databaseProvider: MongoDatabaseProvider
) : MongoRepository<UserIgnored>(
        databaseProvider,
        "users_ignored",
        UserIgnored::class
), IUsersIgnoredRepository {

    override fun fetchByUserId(fetchUsersIgnoredByUserIdDTO: FetchUsersIgnoredByUserIdDTO): List<UserIgnored> {
        val userFriends = mutableListOf<UserIgnored>()

        userFriends.addAll(
                this.mongoCollection.find(
                        Filters.eq("user_id", fetchUsersIgnoredByUserIdDTO.userId)
                )
        )

        return userFriends
    }

    override fun create(createUserFriendDTO: CreateUserFriendDTO) {
        this.mongoCollection.insertOne(
                createUserFriendDTO.userIgnored
        )
    }

    override fun delete(deleteUserFriendDTO: DeleteUserFriendDTO) {
        this.mongoCollection.deleteOne(
                Filters.eq(deleteUserFriendDTO.userIgnored)
        )
    }

}