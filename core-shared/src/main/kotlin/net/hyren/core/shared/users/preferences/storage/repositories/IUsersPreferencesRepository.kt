package net.hyren.core.shared.users.preferences.storage.repositories

import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.preferences.storage.dto.CreateUserPreferencesDTO
import net.hyren.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import net.hyren.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO
import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 * @author SrGutyerrez
 **/
interface IUsersPreferencesRepository : IRepository {

    fun fetchByUserId(
        fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO
    ): Array<Preference>

    fun create(
        createUserPreferencesDTO: CreateUserPreferencesDTO
    ): InsertStatement<Number>

    fun update(updateUserPreferencesDTO: UpdateUserPreferencesDTO)

}