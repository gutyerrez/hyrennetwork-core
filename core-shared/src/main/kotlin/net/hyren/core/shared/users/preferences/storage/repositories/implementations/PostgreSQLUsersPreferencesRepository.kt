package net.hyren.core.shared.users.preferences.storage.repositories.implementations

import net.hyren.core.shared.misc.preferences.PreferenceRegistry
import net.hyren.core.shared.misc.preferences.data.Preference
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
    ): Array<Preference> {
        return transaction {
            val preferences = UsersPreferencesTable.select {
                UsersPreferencesTable.userId eq fetchUserPreferencesByUserIdDTO.userId
            }

            if (preferences.empty()) return@transaction PreferenceRegistry.fetchAll()

            return@transaction preferences.first()[
                    UsersPreferencesTable.preferences
            ]
        }
    }

    override fun create(
        createUserPreferencesDTO: CreateUserPreferencesDTO
    ) {
        transaction {
            UsersPreferencesTable.insert {
                it[userId] = createUserPreferencesDTO.userId
                it[preferences] = createUserPreferencesDTO.preferences
            }
        }
    }

    override fun update(
        updateUserPreferencesDTO: UpdateUserPreferencesDTO
    ) {
        transaction {
            val updated = UsersPreferencesTable.update ({ UsersPreferencesTable.userId eq updateUserPreferencesDTO.userId }) {
                it[preferences] = updateUserPreferencesDTO.preferences
            }

            if (updated <= 0) this@PostgreSQLUsersPreferencesRepository.create(
                CreateUserPreferencesDTO(
                    updateUserPreferencesDTO.userId,
                    updateUserPreferencesDTO.preferences
                )
            )
        }
    }

}