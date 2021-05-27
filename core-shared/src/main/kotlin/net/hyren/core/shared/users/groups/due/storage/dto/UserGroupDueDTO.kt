package net.hyren.core.shared.users.groups.due.storage.dto

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
open class FetchGlobalUserGroupsDueByUserIdDTO(
    val userId: EntityID<UUID>
)

open class FetchUserGroupDueByUserIdDTO(
    val userId: EntityID<UUID>,
)

open class FetchUserGroupDueByUserIdAndServerNameDTO(
    val userId: EntityID<UUID>,
    val server: Server
)

open class CreateUserGroupDueDTO(
    val userId: EntityID<UUID>,
    val group: Group,
    val server: Server?,
    val dueAt: DateTime
)

open class DeleteUserGroupDueDTO(
    val userId: EntityID<UUID>,
    val group: Group,
    val server: Server?
)