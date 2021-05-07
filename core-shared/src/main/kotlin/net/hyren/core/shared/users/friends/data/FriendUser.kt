package net.hyren.core.shared.users.friends.data

import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class FriendUser(
    val userId: EntityID<UUID>,
    val friendUserId: EntityID<UUID>,
    val friendSince: DateTime
)