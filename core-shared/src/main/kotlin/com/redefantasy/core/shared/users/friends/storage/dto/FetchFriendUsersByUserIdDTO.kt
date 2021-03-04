package com.redefantasy.core.shared.users.friends.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchFriendUsersByUserIdDTO(
        val userId: EntityID<UUID>
)