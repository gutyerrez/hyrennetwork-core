package com.redefantasy.core.shared.misc.report.category.storage.table

import com.redefantasy.core.shared.providers.databases.postgres.dao.StringTable

/**
 * @author SrGutyerrez
 **/
object ReportCategoriesTable : StringTable("report_categories") {

    val displayName = varchar("display_name", 255)
    val description = varchar("description", 255)
    val enabled = bool("enabled")

}