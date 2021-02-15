package com.redefantasy.core.shared.users.storage.repositories.implementation

import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.dao.UserDAO
import com.redefantasy.core.shared.users.storage.dto.*
import com.redefantasy.core.shared.users.storage.repositories.IUsersRepository
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresUsersRepository : IUsersRepository {

    override fun fetchById(fetchUserById: FetchUserByIdDTO): User? {
        return transaction {
            var user: User? = null

            val result = UserDAO.find {
                UsersTable.id eq fetchUserById.id
            }

            if (!result.empty()) user = result.first().asUser()

            return@transaction user
        }
    }

    override fun fetchByName(fetchUserByName: FetchUserByNameDTO): User? {
        return transaction {
            var user: User? = null

            val result = UserDAO.find {
                UsersTable.name eq fetchUserByName.name
            }

            if (!result.empty()) user = result.first().asUser()

            return@transaction user
        }
    }

    override fun fetchByDiscordId(fetchUserByDiscordId: FetchUserByDiscordIdDTO): User? {
        return transaction {
            var user: User? = null

            val result = UserDAO.find {
                UsersTable.discordId eq fetchUserByDiscordId.discordId
            }

            if (!result.empty()) user = result.first().asUser()

            return@transaction user
        }
    }

    override fun create(createUserDTO: CreateUserDTO): User {
        return transaction {
            return@transaction UserDAO.new(createUserDTO.id) {
                this.name = createUserDTO.name
                this.lastAddress = createUserDTO.lastAddress
            }.asUser()
        }
    }

    override fun <E> update(updateUserByIdDTO: UpdateUserByIdDTO<E>) {
        transaction {
            val userDAO = UserDAO.find {
                UsersTable.id eq updateUserByIdDTO.id
            }

            if (!userDAO.empty()) updateUserByIdDTO.execute(userDAO.first() as E)
        }
    }

}