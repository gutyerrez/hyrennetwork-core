package com.redefantasy.core.shared.users.preferences.storage.repositories

import com.redefantasy.core.shared.misc.preferences.Preference
import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.preferences.storage.dto.CreateUserPreferencesDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersPreferencesRepository : IRepository {

    fun fetchByUserId(
        fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO
    ): Array<Preference>

    fun create(createUserPreferencesDTO: CreateUserPreferencesDTO)

    fun update(updateUserPreferencesDTO: UpdateUserPreferencesDTO)

}