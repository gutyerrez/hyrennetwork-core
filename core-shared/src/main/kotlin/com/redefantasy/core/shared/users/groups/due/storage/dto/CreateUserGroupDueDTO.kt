package com.redefantasy.core.shared.users.groups.due.storage.dto

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
class CreateUserGroupDueDTO(
    val userId: EntityID<UUID>,
    val group: Group,
    val server: Server?,
    val dueAt: DateTime
)