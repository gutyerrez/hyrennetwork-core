package com.redefantasy.core.shared.misc.report.category.storage.repositories.implementations

import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import com.redefantasy.core.shared.misc.report.category.storage.dao.ReportCategoryDAO
import com.redefantasy.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresReportCategoriesRepository : IReportCategoriesRepository {

    override fun fetchAll(): Map<String, ReportCategory> {
        return transaction {
            val reportCategories = mutableMapOf<String, ReportCategory>()

            ReportCategoryDAO.all().forEach {
                reportCategories[it.id.value] = it.asReportCategory()
            }

            return@transaction reportCategories
        }
    }

}