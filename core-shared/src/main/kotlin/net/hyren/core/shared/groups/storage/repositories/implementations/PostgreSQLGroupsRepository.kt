package net.hyren.core.shared.groups.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.groups.storage.dao.GroupDAO
import net.hyren.core.shared.groups.storage.repositories.IGroupsRepository
import net.hyren.core.shared.groups.storage.table.GroupsTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLGroupsRepository : IGroupsRepository {

    override fun fetchAll() = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        Group.values().forEach {
            println("Load data from $it")

            GroupDAO.find {
                GroupsTable.id eq it.name
            }.firstOrNull()?.readGroup()
        }
    }

}