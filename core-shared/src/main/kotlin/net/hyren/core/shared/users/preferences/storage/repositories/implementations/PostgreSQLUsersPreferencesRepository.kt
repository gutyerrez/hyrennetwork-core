package net.hyren.core.shared.users.preferences.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.users.preferences.storage.dto.CreateUserPreferencesDTO
import net.hyren.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import net.hyren.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO
import net.hyren.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import net.hyren.core.shared.users.preferences.storage.table.UsersPreferencesTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * @author SrGutyerrez
 **/
class PostgreSQLUsersPreferencesRepository : IUsersPreferencesRepository {

    override fun fetchByUserId(
        fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UsersPreferencesTable.select {
            UsersPreferencesTable.userId eq fetchUserPreferencesByUserIdDTO.userId
        }.firstOrNull()?.get(UsersPreferencesTable.preferences) ?: emptyArray()
    }

    override fun create(
        createUserPreferencesDTO: CreateUserPreferencesDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UsersPreferencesTable.insert {
            it[userId] = createUserPreferencesDTO.userId
            it[preferences] = createUserPreferencesDTO.preferences
        }
    }

    override fun update(
        updateUserPreferencesDTO: UpdateUserPreferencesDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        if (UsersPreferencesTable.update ({ UsersPreferencesTable.userId eq updateUserPreferencesDTO.userId }) {
            it[preferences] = updateUserPreferencesDTO.preferences
        } <= 0) {
            create(
                CreateUserPreferencesDTO(
                    updateUserPreferencesDTO.userId, updateUserPreferencesDTO.preferences
                )
            )
        }
    }

}