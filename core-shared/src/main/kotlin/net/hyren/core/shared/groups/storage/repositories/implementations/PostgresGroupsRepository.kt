package net.hyren.core.shared.groups.storage.repositories.implementations

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.groups.storage.dao.GroupDAO
import net.hyren.core.shared.groups.storage.repositories.IGroupsRepository
import net.hyren.core.shared.groups.storage.table.GroupsTable
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