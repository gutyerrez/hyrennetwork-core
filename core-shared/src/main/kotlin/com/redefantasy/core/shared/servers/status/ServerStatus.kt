package com.redefantasy.core.shared.servers.status

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.servers.data.Server
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