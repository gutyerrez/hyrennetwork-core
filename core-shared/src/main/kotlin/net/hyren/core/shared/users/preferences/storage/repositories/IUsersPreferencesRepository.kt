package net.hyren.core.shared.users.preferences.storage.repositories

import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.preferences.storage.dto.CreateUserPreferencesDTO
import net.hyren.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import net.hyren.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO

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