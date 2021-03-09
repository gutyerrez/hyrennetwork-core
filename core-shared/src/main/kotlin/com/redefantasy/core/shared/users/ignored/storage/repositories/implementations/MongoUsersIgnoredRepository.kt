package com.redefantasy.core.shared.users.ignored.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.ignored.data.IgnoredUser
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchIgnoredUsersByUserIdDTO
import com.redefantasy.core.shared.users.ignored.storage.repositories.IUsersIgnoredRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersIgnoredRepository : MongoRepository<IgnoredUser>(
    CoreProvider.Databases.Mongo.MONGO_MAIN,
    "users_ignored",
    IgnoredUser::class
), IUsersIgnoredRepository {

    override fun fetchByUserId(
        fetchIgnoredUsersByUserIdDTO: FetchIgnoredUsersByUserIdDTO
    ): List<IgnoredUser> {
        val userFriends = mutableListOf<IgnoredUser>()

        userFriends.addAll(
            this.mongoCollection.find(
                Filters.eq("user_id", fetchIgnoredUsersByUserIdDTO.userId)
            )
        )

        return userFriends
    }

    override fun create(createIgnoredUserDTO: CreateIgnoredUserDTO) {
        this.mongoCollection.insertOne(
            createIgnoredUserDTO.ignoredUser
        )
    }

    override fun delete(deleteIgnoredUserDTO: DeleteIgnoredUserDTO) {
        this.mongoCollection.deleteOne(
            Filters.and(
                Filters.eq("user_id", deleteIgnoredUserDTO.userId),
                Filters.eq("ignored_user_id", deleteIgnoredUserDTO.userId)
            )
        )
    }

}