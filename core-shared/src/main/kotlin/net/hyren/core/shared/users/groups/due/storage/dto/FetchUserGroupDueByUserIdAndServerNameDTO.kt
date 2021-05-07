package net.hyren.core.shared.users.groups.due.storage.dto

import net.hyren.core.shared.servers.data.Server
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserGroupDueByUserIdAndServerNameDTO(
        val id: UUID,
        val server: Server
)