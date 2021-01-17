package com.redefantasy.core.shared.misc.report.category.storage.repositories

import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IReportCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, ReportCategory>

}