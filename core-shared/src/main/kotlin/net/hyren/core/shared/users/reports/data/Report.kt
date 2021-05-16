package net.hyren.core.shared.users.reports.data

import net.hyren.core.shared.misc.report.category.data.ReportCategory
import net.hyren.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class Report(
    val reporterId: EntityID<UUID>,
    val reportCategory: ReportCategory,
    val reportedAt: DateTime,
    val server: Server
)