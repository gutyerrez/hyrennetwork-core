package com.redefantasy.core.shared.misc.maintenance.repositories.implementations

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import com.redefantasy.core.shared.misc.maintenance.storage.table.MaintenanceTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * @author Gutyerrez
 */
class PostgresMaintenanceRepository : IMaintenanceRepository {

    override fun fetchByApplication(
        application: Application
    ): Boolean {
        return transaction {
            val result = MaintenanceTable.selectAll()

            if (result.empty()) return@transaction false

            return@transaction result.first()[MaintenanceTable.currentState]
        }
    }

    override fun update(
        application: Application,
        newState: Boolean
    ) {
        transaction {
            val updated = MaintenanceTable.update({ MaintenanceTable.applicationName eq application.name }) {
                it[currentState] = newState
            }

            if (updated <= 0) MaintenanceTable.insert {
                it[applicationName] = application.name
                it[currentState] = newState
            }
        }
    }

}