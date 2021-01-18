package com.redefantasy.core.shared.users.groups.due.storage.repositories.implementations

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.groups.due.storage.dao.UserGroupDueDAO
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
    ): List<Group> {
        return transaction {
            val groups = mutableListOf<Group>()


            UserGroupDueDAO.find {
                UsersGroupsDueTable.userId eq fetchUserGroupDueByUserIdDTO.id and (
                        UsersGroupsDueTable.dueAt greater DateTime.now()
                )
            }.forEach { groups.add(it.group()) }

            return@transaction groups
        }
    }

    override fun fetchUsersGroupsDueByUserIdAndServerName(
            fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ): List<Group> {
        return transaction {
            val groups = mutableListOf<Group>()

            UserGroupDueDAO.find {
                UsersGroupsDueTable.userId eq fetchUserGroupDueByUserIdAndServerNameDTO.id and (
                        UsersGroupsDueTable.serverName eq fetchUserGroupDueByUserIdAndServerNameDTO.server.name
                        ) and (
                        UsersGroupsDueTable.dueAt greater DateTime.now()
                        )
            }.forEach { groups.add(it.group()) }

            return@transaction groups
        }
    }

}