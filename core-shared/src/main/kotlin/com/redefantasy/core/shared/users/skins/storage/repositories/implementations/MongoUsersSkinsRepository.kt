package com.redefantasy.core.shared.users.skins.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByUserIdDTO
import com.redefantasy.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import org.bson.Document

/**
 * @author SrGutyerrez
 **/
class MongoUsersSkinsRepository(
        databaseProvider: MongoDatabaseProvider
) : MongoRepository<Document>(
        databaseProvider,
        "users_skins",
        Document::class
), IUsersSkinsRepository {

    override fun fetchByUserId(fetchUserSkinByUserIdDTO: FetchUserSkinByUserIdDTO): List<UserSkin> {
        val skins = mutableListOf<UserSkin>()

        this.mongoCollection.find(Filters.eq(
                "user_id", fetchUserSkinByUserIdDTO.userId
        )).forEach {
            val userSkin = it["user_skin"] as UserSkin

            skins.add(userSkin)
        }

        return skins
    }

    override fun fetchByName(fetchUserSkinByNameDTO: FetchUserSkinByNameDTO): UserSkin? {
        TODO("Not yet implemented")
    }

    override fun create(createUserSkinDTO: CreateUserSkinDTO) {
        val document = Document("user_id", createUserSkinDTO.userId)
                .append("user_skin", createUserSkinDTO.userSkin)

        this.mongoCollection.insertOne(document)
    }

}