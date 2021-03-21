package com.redefantasy.core.shared.misc.maintenance.storage.table

import com.redefantasy.core.shared.applications.storage.table.ApplicationsTable
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object MaintenanceTable : Table("maintenance") {

    val applicationName = reference("application_name", ApplicationsTable)
    var current_state = bool("current_state")

}