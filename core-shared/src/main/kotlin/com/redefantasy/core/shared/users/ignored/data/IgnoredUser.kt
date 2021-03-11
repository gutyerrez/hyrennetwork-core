package com.redefantasy.core.shared.users.ignored.data

import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class IgnoredUser(
    val userId: EntityID<UUID>,
    val ignoredUserId: EntityID<UUID>,
    val ignoredSince: DateTime
)