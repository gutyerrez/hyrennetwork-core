package com.redefantasy.core.shared.users.passwords.storage.repositories.implementations

import com.redefantasy.core.shared.users.passwords.data.UserPassword
import com.redefantasy.core.shared.users.passwords.storage.dao.UserPasswordDAO
import com.redefantasy.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import com.redefantasy.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import com.redefantasy.core.shared.users.passwords.storage.table.UserPasswordTable
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgresUsersPasswordsRepository : IUsersPasswordsRepository {

    override fun fetchByUserId(fetchUserPasswordByUserIdDTO: FetchUserPasswordByUserIdDTO): List<UserPassword> {
        return transaction {
            val userPasswords = mutableListOf<UserPassword>()

            UserPasswordDAO.find {
                UserPasswordTable.userId eq fetchUserPasswordByUserIdDTO.userId
            }.forEach { userPasswords.add(it.asUserPassword()) }

            return@transaction userPasswords
        }
    }

    override fun create(createUserPasswordDTO: CreateUserPasswordDTO) {
        transaction {
            UserPasswordDAO.find {
                UserPasswordTable.userId eq createUserPasswordDTO.userId and (
                    UserPasswordTable.enabled eq true
                )
            }.forEach { it.enabled = false }

            UserPasswordDAO.new {
                this.userId = EntityID(
                    createUserPasswordDTO.userId,
                    UsersTable
                )
                this.password = createUserPasswordDTO.password
            }
        }
    }

}