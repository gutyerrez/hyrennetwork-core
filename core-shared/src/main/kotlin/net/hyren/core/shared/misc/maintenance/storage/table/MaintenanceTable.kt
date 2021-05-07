package net.hyren.core.shared.misc.maintenance.storage.table

import net.hyren.core.shared.applications.storage.table.ApplicationsTable
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object MaintenanceTable : Table("maintenance") {

    val applicationName = reference("application_name", ApplicationsTable)
    var currentState = bool("current_state")

}