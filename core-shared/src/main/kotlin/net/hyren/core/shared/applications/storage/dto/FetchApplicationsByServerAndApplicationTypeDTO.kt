package net.hyren.core.shared.applications.storage.dto

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.servers.data.Server

/**
 * @author SrGutyerrez
 **/
class FetchApplicationsByServerAndApplicationTypeDTO(
    val server: Server,
    val applicationType: ApplicationType
)