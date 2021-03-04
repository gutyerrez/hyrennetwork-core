package com.redefantasy.core.shared.users.friends.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FetchFriendRequestsByUserIdDTO(
    val userId: EntityID<UUID>
)