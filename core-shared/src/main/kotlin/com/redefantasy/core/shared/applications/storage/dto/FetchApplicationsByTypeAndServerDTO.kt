package com.redefantasy.core.shared.applications.storage.dto

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.servers.data.Server

/**
 * @author SrGutyerrez
 **/
class FetchApplicationsByTypeAndServerDTO(
        val applicationType: ApplicationType,
        val server: Server
)