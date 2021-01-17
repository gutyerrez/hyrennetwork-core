package com.redefantasy.core.shared.groups.storage.repositories.implementations

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.groups.storage.dao.GroupDAO
import com.redefantasy.core.shared.groups.storage.repositories.IGroupsRepository
import com.redefantasy.core.shared.groups.storage.table.GroupsTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresGroupsRepository : IGroupsRepository {

    override fun fetchAll() {
        transaction {
            Group.values().forEach {
                val result = GroupDAO.find {
                    GroupsTable.id eq it.name
                }

                if (!result.empty()) result.first().asGroup()
            }
        }
    }

}