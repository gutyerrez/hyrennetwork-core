package com.redefantasy.core.shared.users.preferences.storage.repositories.implementations

import com.redefantasy.core.shared.misc.preferences.PreferenceRegistry
import com.redefantasy.core.shared.misc.preferences.data.Preference
import com.redefantasy.core.shared.users.preferences.storage.dto.CreateUserPreferencesDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.FetchUserPreferencesByUserIdDTO
import com.redefantasy.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO
import com.redefantasy.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import com.redefantasy.core.shared.users.preferences.storage.table.UsersPreferencesTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * @author SrGutyerrez
 **/
class PostgresUsersPreferencesRepository : IUsersPreferencesRepository {

    override fun fetchByUserId(
        fetchUserPreferencesByUserIdDTO: FetchUserPreferencesByUserIdDTO
    ): Array<Preference<*>> {
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

            if (updated <= 0) this@PostgresUsersPreferencesRepository.create(
                CreateUserPreferencesDTO(
                    updateUserPreferencesDTO.userId,
                    updateUserPreferencesDTO.preferences
                )
            )
        }
    }

}