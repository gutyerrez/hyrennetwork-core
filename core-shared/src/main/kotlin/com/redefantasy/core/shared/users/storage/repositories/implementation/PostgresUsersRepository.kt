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
            return@transaction UserDAO.find {
                UsersTable.id eq fetchUserById.id
            }.firstOrNull()?.asUser()
        }
    }

    override fun fetchByName(fetchUserByName: FetchUserByNameDTO): User? {
        return transaction {
            return@transaction UserDAO.find {
                UsersTable.name ilike fetchUserByName.name
            }.firstOrNull()?.asUser()
        }
    }

    override fun fetchByDiscordId(fetchUserByDiscordId: FetchUserByDiscordIdDTO): User? {
        return transaction {
            return@transaction UserDAO.find {
                UsersTable.discordId eq fetchUserByDiscordId.discordId
            }.firOrNull()?.asUser()
        }
    }

    override fun fetchByLastAddress(fetchUserByLastAddress: FetchUserByLastAddressDTO): List<User> {
        return transaction {
            return@transaction UserDAO.find {
                UsersTable.lastAddress eq fetchUserByLastAddress.lastAddress
            }.map { it.asUser() }
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
            UserDAO.find {
                UsersTable.id eq updateUserByIdDTO.id
            }.map { updateUserByIdDTO.execute.accept(it) }
        }
    }

}