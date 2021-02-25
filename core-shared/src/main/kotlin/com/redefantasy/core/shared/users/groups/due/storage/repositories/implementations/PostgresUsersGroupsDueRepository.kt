package com.redefantasy.core.shared.users.groups.due.storage.repositories.implementations

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.groups.due.storage.dao.UserGroupDueDAO
import com.redefantasy.core.shared.users.groups.due.storage.dto.CreateUserGroupDueDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.DeleteUserGroupDueDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO
import com.redefantasy.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import com.redefantasy.core.shared.users.groups.due.storage.table.UsersGroupsDueTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
class PostgresUsersGroupsDueRepository : IUsersGroupsDueRepository {

    override fun fetchUsersGroupsDueByUserId(
        fetchUserGroupDueByUserIdDTO: FetchUserGroupDueByUserIdDTO
    ): Map<Server?, List<Group>> {
        return transaction {
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

            return@transaction groups
        }
    }

    override fun fetchUsersGroupsDueByUserIdAndServerName(
        fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ): Map<Server?, List<Group>> {
        return transaction {
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

            return@transaction groups
        }
    }

    override fun create(createUserGroupDueDTO: CreateUserGroupDueDTO) {
        transaction {
            UserGroupDueDAO.new {
                this.userId = createUserGroupDueDTO.userId
                this.serverName = createUserGroupDueDTO.server?.name
                this.group = createUserGroupDueDTO.group
                this.dueAt = createUserGroupDueDTO.dueAt
            }
        }
    }

    override fun delete(deleteUserGroupDueDTO: DeleteUserGroupDueDTO): Boolean {
        return transaction {
            val userGroupDue = UserGroupDueDAO.find {
                UsersGroupsDueTable.userId eq deleteUserGroupDueDTO.userId and (
                        UsersGroupsDueTable.serverName eq deleteUserGroupDueDTO.server?.name
                ) and (
                        UsersGroupsDueTable.group eq deleteUserGroupDueDTO.group
                )
            }

            if (userGroupDue.empty()) return@transaction false

            userGroupDue.first().delete()

            return@transaction true
        }
    }

}