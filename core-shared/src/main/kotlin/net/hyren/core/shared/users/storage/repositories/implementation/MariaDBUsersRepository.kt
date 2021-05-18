package net.hyren.core.shared.users.storage.repositories.implementation

import net.hyren.core.shared.users.data.User
import net.hyren.core.shared.users.storage.dao.UserDAO
import net.hyren.core.shared.users.storage.dto.*
import net.hyren.core.shared.users.storage.repositories.IUsersRepository
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class MariaDBUsersRepository : IUsersRepository {

    override fun fetchById(fetchUserById: FetchUserByIdDTO): User? {
        return transaction {
            addLogger(StdOutSqlLogger)

            return@transaction UserDAO.find {
                println("eq: ${UsersTable.id eq fetchUserById.id}")

                println("UsersTable.id: ${UsersTable.id}")
                println("FetchUserById.id: ${fetchUserById.id}")


                UsersTable.id eq fetchUserById.id
            }.firstOrNull()?.asUser()
        }
    }

    override fun fetchByName(fetchUserByName: FetchUserByNameDTO): User? {
        return transaction {
            addLogger(StdOutSqlLogger)

            return@transaction UserDAO.find {
                UsersTable.name like fetchUserByName.name
            }.firstOrNull()?.asUser()
        }
    }

    override fun fetchByDiscordId(fetchUserByDiscordId: FetchUserByDiscordIdDTO): User? {
        return transaction {
            addLogger(StdOutSqlLogger)

            return@transaction UserDAO.find {
                UsersTable.discordId eq fetchUserByDiscordId.discordId
            }.firstOrNull()?.asUser()
        }
    }

    override fun fetchByLastAddress(fetchUserByLastAddress: FetchUserByLastAddressDTO): List<User> {
        return transaction {
            addLogger(StdOutSqlLogger)

            return@transaction UserDAO.find {
                UsersTable.lastAddress eq fetchUserByLastAddress.lastAddress
            }.map { it.asUser() }
        }
    }

    override fun create(createUserDTO: CreateUserDTO): User {
        return transaction {
            addLogger(StdOutSqlLogger)

            return@transaction UserDAO.new(createUserDTO.id) {
                this.name = createUserDTO.name
                this.lastAddress = createUserDTO.lastAddress
            }.asUser()
        }
    }

    override fun update(updateUserByIdDTO: UpdateUserByIdDTO) {
        transaction {
            addLogger(StdOutSqlLogger)

            UserDAO.find {
                UsersTable.id eq updateUserByIdDTO.id
            }.map { updateUserByIdDTO.execute.accept(it) }
        }
    }

}