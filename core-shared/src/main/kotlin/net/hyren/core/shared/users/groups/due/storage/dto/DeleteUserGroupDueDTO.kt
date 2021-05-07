package net.hyren.core.shared.users.groups.due.storage.dto

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
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