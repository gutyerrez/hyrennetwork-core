package net.hyren.core.shared.users.reports.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.misc.kotlin.DateTimeSerializer
import net.hyren.core.shared.misc.kotlin.EntityIDSerializer
import net.hyren.core.shared.misc.kotlin.ServerSerializer
import net.hyren.core.shared.misc.kotlin.UUIDSerializer
import net.hyren.core.shared.misc.report.category.data.ReportCategory
import net.hyren.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
@Serializable
data class Report(
    @Serializable(EntityIDSerializer::class)
    val reporter: EntityID<@Serializable(UUIDSerializer::class) UUID>,
    @SerialName("report_category") val reportCategory: ReportCategory,
    @Serializable(DateTimeSerializer::class)
    @SerialName("reported_at") val reportedAt: DateTime,
    @Serializable(ServerSerializer::class)
    @SerialName("server") val server: Server
)