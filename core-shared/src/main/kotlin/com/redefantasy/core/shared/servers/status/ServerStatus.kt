package com.redefantasy.core.shared.servers.status

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.servers.data.Server
import org.influxdb.dto.Point
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class ServerStatus(
        applicationName: String,
        applicationType: ApplicationType,
        server: Server,
        address: InetSocketAddress,
        onlineSince: Long
) : ApplicationStatus(
    applicationName,
    applicationType,
    server,
    address,
    onlineSince
) {

    var loginsCount = 0

    override fun buildPoint(): Point.Builder = super.buildPoint()
            .addField("logins_count", this.loginsCount)

}