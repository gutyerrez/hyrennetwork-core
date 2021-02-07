package com.redefantasy.core.shared.misc.report.category.storage.dao

import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import com.redefantasy.core.shared.misc.report.category.storage.table.ReportCategoriesTable
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntity
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class ReportCategoryDAO(
        name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<ReportCategoryDAO>(ReportCategoriesTable)

    val displayName by ReportCategoriesTable.displayName
    val description by ReportCategoriesTable.description
    val enabled by ReportCategoriesTable.enabled

    fun asReportCategory() = ReportCategory(
            this.id,
            this.displayName,
            this.description,
            this.enabled
    )

}