package com.redefantasy.core.shared.users.groups.due.storage.dto

import com.redefantasy.core.shared.servers.data.Server
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserGroupDueByUserIdAndServerNameDTO(
        val id: UUID,
        val server: Server
)