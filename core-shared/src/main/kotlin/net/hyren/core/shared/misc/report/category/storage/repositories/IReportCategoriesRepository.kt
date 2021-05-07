package net.hyren.core.shared.misc.report.category.storage.repositories

import net.hyren.core.shared.misc.report.category.data.ReportCategory
import net.hyren.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IReportCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, ReportCategory>

}