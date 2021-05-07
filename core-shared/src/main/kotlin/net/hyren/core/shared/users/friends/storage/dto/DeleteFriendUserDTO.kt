package net.hyren.core.shared.users.friends.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class DeleteFriendUserDTO(
        val userId: EntityID<UUID>,
        val friendId: EntityID<UUID>
)