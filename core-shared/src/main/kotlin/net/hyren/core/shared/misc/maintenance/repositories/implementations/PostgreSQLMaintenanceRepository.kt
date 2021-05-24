package net.hyren.core.shared.misc.maintenance.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import net.hyren.core.shared.misc.maintenance.storage.table.MaintenanceTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * @author Gutyerrez
 */
class PostgreSQLMaintenanceRepository : IMaintenanceRepository {

    override fun fetchByApplication(
        application: Application
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        MaintenanceTable.select {
            MaintenanceTable.applicationName eq application.name
        }.firstOrNull()?.get(MaintenanceTable.currentState) ?: false
    }

    override fun update(
        application: Application,
        newState: Boolean
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        if (MaintenanceTable.update({ MaintenanceTable.applicationName eq application.name }) {
            it[currentState] = newState
        } <= 0) {
            MaintenanceTable.insert {
                it[applicationName] = application.name
                it[currentState] = newState
            }
        }
    }

}