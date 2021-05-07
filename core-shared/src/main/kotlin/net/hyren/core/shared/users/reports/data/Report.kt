package net.hyren.core.shared.users.reports.data

import net.hyren.core.shared.misc.report.category.data.ReportCategory
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