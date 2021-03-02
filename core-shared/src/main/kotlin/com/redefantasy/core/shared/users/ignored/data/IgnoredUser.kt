package com.redefantasy.core.shared.users.ignored.data

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class IgnoredUser @BsonCreator constructor(
        @param:BsonProperty("user_id")
        @field:BsonProperty("user_id")
        val userId: EntityID<UUID>,
        @param:BsonProperty("ignored_user_id")
        @field:BsonProperty("ignored_user_id")
        val ignoredUserId: EntityID<UUID>,
        @param:BsonProperty("ignored_since")
        @field:BsonProperty("ignored_since")
        val ignoredSince: DateTime
)