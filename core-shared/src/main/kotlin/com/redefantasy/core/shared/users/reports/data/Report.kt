package com.redefantasy.core.shared.users.reports.data

import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class Report(
        val reporter: UUID,
        val reportCategory: ReportCategory,
        val reportedAt: Long,
        val serverName: String
)