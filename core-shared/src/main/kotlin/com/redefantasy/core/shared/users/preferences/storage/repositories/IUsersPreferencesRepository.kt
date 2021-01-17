package com.redefantasy.core.shared.users.preferences.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.preferences.data.UserPreference
import com.redefantasy.core.shared.users.preferences.storage.dto.CreateUserPreferenceDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersPreferencesRepository : IRepository {

    fun fetchByUserId(fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO): List<UserPreference>

    fun create(createUserPreferenceDTO: CreateUserPreferenceDTO)

    fun update(updateUserPreferenceDTO: CreateUserPreferenceDTO)

}