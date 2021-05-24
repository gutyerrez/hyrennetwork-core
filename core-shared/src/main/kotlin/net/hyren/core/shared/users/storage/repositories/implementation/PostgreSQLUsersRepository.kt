package net.hyren.core.shared.users.storage.repositories.implementation

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.users.storage.dao.UserDAO
import net.hyren.core.shared.users.storage.dto.*
import net.hyren.core.shared.users.storage.repositories.IUsersRepository
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLUsersRepository : IUsersRepository {

    override fun fetchById(
        fetchUserById: FetchUserByIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.find {
            UsersTable.id eq fetchUserById.id
        }.firstOrNull()?.toUser()
    }

    override fun fetchByName(
        fetchUserByName: FetchUserByNameDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.find {
                UsersTable.name like fetchUserByName.name
        }.firstOrNull()?.toUser()
    }

    override fun fetchByDiscordId(
        fetchUserByDiscordId: FetchUserByDiscordIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.find {
            UsersTable.discordId eq fetchUserByDiscordId.discordId
        }.firstOrNull()?.toUser()
    }

    override fun fetchByLastAddress(
        fetchUserByLastAddress: FetchUserByLastAddressDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.find {
            UsersTable.lastAddress eq fetchUserByLastAddress.lastAddress
        }.map { it.toUser() }
    }

    override fun create(
        createUserDTO: CreateUserDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.new(createUserDTO.id) {
            this.name = createUserDTO.name
            this.lastAddress = createUserDTO.lastAddress
        }.toUser()
    }

    override fun update(
        updateUserByIdDTO: UpdateUserByIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserDAO.find {
            UsersTable.id eq updateUserByIdDTO.id
        }.forEach { updateUserByIdDTO.execute(it) }
    }

}