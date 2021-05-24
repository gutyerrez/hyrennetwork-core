package net.hyren.core.shared.users.passwords.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.users.passwords.storage.dao.UserPasswordDAO
import net.hyren.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import net.hyren.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import net.hyren.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import net.hyren.core.shared.users.passwords.storage.table.UsersPasswordsTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgreSQLUsersPasswordsRepository : IUsersPasswordsRepository {

    override fun fetchByUserId(
        fetchUserPasswordByUserIdDTO: FetchUserPasswordByUserIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPasswordDAO.find {
            UsersPasswordsTable.userId eq fetchUserPasswordByUserIdDTO.userId
        }.map { it.asUserPassword() }
    }

    override fun create(
        createUserPasswordDTO: CreateUserPasswordDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPasswordDAO.find {
            UsersPasswordsTable.userId eq createUserPasswordDTO.userId and (
                UsersPasswordsTable.enabled eq true
            )
        }.forEach { it.enabled = false }

        UserPasswordDAO.new {
            this.userId = createUserPasswordDTO.userId
            this.password = createUserPasswordDTO.password
        }
    }

}