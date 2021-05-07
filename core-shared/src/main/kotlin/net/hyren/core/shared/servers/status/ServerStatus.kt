package net.hyren.core.shared.servers.status

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.servers.data.Server
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
)