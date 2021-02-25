package com.redefantasy.core.shared.users.groups.due.storage.dto

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class DeleteUserGroupDueDTO(
        val userId: EntityID<UUID>,
        val group: Group,
        val server: Server?
)