package net.hyren.core.shared.misc.report.category.storage.table

import net.hyren.core.shared.providers.databases.postgresql.dao.StringTable

/**
 * @author SrGutyerrez
 **/
object ReportCategoriesTable : StringTable("report_categories") {

    val displayName = varchar("display_name", 255)
    val description = varchar("description", 255)
    val enabled = bool("enabled")

}