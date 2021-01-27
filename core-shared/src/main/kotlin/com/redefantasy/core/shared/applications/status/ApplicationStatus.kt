package com.redefantasy.core.shared.applications.status

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.servers.data.Server
import org.influxdb.dto.Point
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
open class ApplicationStatus(
        val applicationName: String,
        val applicationType: ApplicationType,
        val server: Server?,
        val address: InetSocketAddress,
        val onlineSince: Long
) {

    var heapSize: Long? = null
    var heapMaxSize: Long? = null
    var heapFreeSize: Long? = null
    var onlinePlayers: Int = 0
    var maintenance: Boolean = false

    fun isSame(applicationName: String) = this.applicationName == applicationName

    fun isSameServer(other: ApplicationStatus) = this.server != null && this.server == other.server

    open fun buildPoint(): Point.Builder {
        val builder = Point.measurement("application_status")
                .tag("application_name", this.applicationName)
                .tag("application_type", this.applicationType.name)

        if (this.server != null) {
            builder.tag("server_name", this.server.name)
        }

        builder.addField("address", String.format(
                "%s:%d", this.address.address.hostAddress, this.address.port
        )).addField("online_since", this.onlineSince)
                .addField("heap_size", this.heapSize)
                .addField("heap_max_size", this.heapMaxSize)
                .addField("heap_free_size", this.heapFreeSize)
                .addField("maintenance", this.maintenance)
                .addField("online_players", this.onlinePlayers)

        return builder
    }

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as ApplicationStatus

        if (applicationName != other.applicationName) return false

        return true
    }

    override fun hashCode(): Int {
        return this.applicationName.hashCode()
    }

}