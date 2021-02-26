package com.redefantasy.core.shared.users.storage.repositories.implementation

import com.redefantasy.core.shared.misc.exposed.ilike
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
                UsersTable.name ilike fetchUserByName.name
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

    override fun fetchByLastAddress(fetchUserByLastAddress: FetchUserByLastAddressDTO): List<User> {
        return transaction {
            val users = mutableListOf<User>()

            UserDAO.find {
                UsersTable.lastAddress eq fetchUserByLastAddress.lastAddress
            }.forEach { users.add(it.asUser()) }

            return@transaction users
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

    override fun update(updateUserByIdDTO: UpdateUserByIdDTO) {
        transaction {
            val user = UserDAO.find {
                UsersTable.id eq updateUserByIdDTO.id
            }

            if (!user.empty()) updateUserByIdDTO.execute.accept(user.first())
        }
    }

}