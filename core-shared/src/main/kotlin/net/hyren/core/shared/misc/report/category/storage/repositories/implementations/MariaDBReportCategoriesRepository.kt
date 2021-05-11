package net.hyren.core.shared.misc.report.category.storage.repositories.implementations

import net.hyren.core.shared.misc.report.category.data.ReportCategory
import net.hyren.core.shared.misc.report.category.storage.dao.ReportCategoryDAO
import net.hyren.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class MariaDBReportCategoriesRepository : IReportCategoriesRepository {

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