package com.redefantasy.core.shared.users.preferences.storage.repositories.implementations

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import com.redefantasy.core.shared.providers.databases.mongo.repositories.MongoRepository
import com.redefantasy.core.shared.users.preferences.data.UserPreference
import com.redefantasy.core.shared.users.preferences.storage.dto.CreateUserPreferenceDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import com.redefantasy.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersPreferencesRepository(
        databaseProvider: MongoDatabaseProvider
) : MongoRepository<UserPreference>(
        databaseProvider,
        "users_preferences",
        UserPreference::class
), IUsersPreferencesRepository {

    override fun fetchByUserId(fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO): List<UserPreference> {
        val preferences = mutableListOf<UserPreference>()

        this.mongoCollection.find(
                Filters.eq("user_id", fetchUserPreferencesByUserIdDTO.userId)
        ).forEach { preferences.add(it) }

        return preferences
    }

    override fun create(createUserPreferenceDTO: CreateUserPreferenceDTO) {
        this.mongoCollection.insertOne(
                createUserPreferenceDTO.userPreference
        )
    }

    override fun update(updateUserPreferenceDTO: CreateUserPreferenceDTO) {
        this.mongoCollection.updateOne(
                Filters.and(
                        Filters.eq("user_id", updateUserPreferenceDTO.userPreference.userId),
                        Filters.eq("preference", updateUserPreferenceDTO.userPreference.preference)
                ),
                Updates.set(
                        "status",
                        updateUserPreferenceDTO.userPreference
                )
        )
    }

}