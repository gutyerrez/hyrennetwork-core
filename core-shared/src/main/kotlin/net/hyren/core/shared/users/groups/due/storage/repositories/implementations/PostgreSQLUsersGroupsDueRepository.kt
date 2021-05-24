package net.hyren.core.shared.users.groups.due.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.users.groups.due.storage.dao.UserGroupDueDAO
import net.hyren.core.shared.users.groups.due.storage.dto.CreateUserGroupDueDTO
import net.hyren.core.shared.users.groups.due.storage.dto.DeleteUserGroupDueDTO
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO
import net.hyren.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import net.hyren.core.shared.users.groups.due.storage.table.UsersGroupsDueTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

/**
 * @author SrGutyerrez
 **/
class PostgreSQLUsersGroupsDueRepository : IUsersGroupsDueRepository {

    override fun fetchUsersGroupsDueByUserId(
        fetchUserGroupDueByUserIdDTO: FetchUserGroupDueByUserIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        val groups = mutableMapOf<Server?, MutableList<Group>>()

        UserGroupDueDAO.find {
            UsersGroupsDueTable.userId eq fetchUserGroupDueByUserIdDTO.id and (
                UsersGroupsDueTable.dueAt greater DateTime.now()
            )
        }.forEach {
            val server = it.server()

            val currentGroups = groups.getOrDefault(server, mutableListOf())

            currentGroups.add(it.group)

            groups[server] = currentGroups
        }

        groups
    }

    override fun fetchUsersGroupsDueByUserIdAndServerName(
        fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        val groups = mutableMapOf<Server?, MutableList<Group>>()

        UserGroupDueDAO.find {
            UsersGroupsDueTable.userId eq fetchUserGroupDueByUserIdAndServerNameDTO.id and (
                UsersGroupsDueTable.serverName eq fetchUserGroupDueByUserIdAndServerNameDTO.server.name
            ) and (
                UsersGroupsDueTable.dueAt greater DateTime.now()
            )
        }.forEach {
            val server = it.server()

            val currentGroups = groups.getOrDefault(server, mutableListOf())

            currentGroups.add(it.group)

            groups[server] = currentGroups
        }

        groups
    }

    override fun create(createUserGroupDueDTO: CreateUserGroupDueDTO) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserGroupDueDAO.new {
            this.userId = createUserGroupDueDTO.userId
            this.serverName = createUserGroupDueDTO.server?.name
            this.group = createUserGroupDueDTO.group
            this.dueAt = createUserGroupDueDTO.dueAt
        }
    }

    override fun delete(deleteUserGroupDueDTO: DeleteUserGroupDueDTO) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UsersGroupsDueTable.deleteWhere {
            UsersGroupsDueTable.userId eq deleteUserGroupDueDTO.userId and (
                UsersGroupsDueTable.serverName eq deleteUserGroupDueDTO.server?.name
            ) and (
                UsersGroupsDueTable.group eq deleteUserGroupDueDTO.group
            )
        } != 0
    }

}