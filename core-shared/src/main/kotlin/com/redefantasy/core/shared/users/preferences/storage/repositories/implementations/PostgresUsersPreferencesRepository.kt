package com.redefantasy.core.shared.users.preferences.storage.repositories.implementations

import com.redefantasy.core.shared.users.preferences.data.UserPreference
import com.redefantasy.core.shared.users.preferences.storage.dto.CreateUserPreferenceDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import com.redefantasy.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository

/**
 * @author SrGutyerrez
 **/
class PostgresUsersPreferencesRepository : IUsersPreferencesRepository {

    override fun fetchByUserId(
        fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO
    ): List<UserPreference> {
        TODO("not implemented")
    }

    override fun create(
        createUserPreferenceDTO: CreateUserPreferenceDTO
    ) {
        TODO("not implemented")
    }

    override fun update(
        updateUserPreferenceDTO: CreateUserPreferenceDTO
    ) {
        TODO("not implemented")
    }

}