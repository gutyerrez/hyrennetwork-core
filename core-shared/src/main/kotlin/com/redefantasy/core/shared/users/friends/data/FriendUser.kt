package com.redefantasy.core.shared.users.friends.data

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class FriendUser @BsonCreator constructor(
        @param:BsonProperty("user_id")
        @field:BsonProperty("user_id")
        val userId: EntityID<UUID>,
        @param:BsonProperty("friend_user_id")
        @field:BsonProperty("friend_user_id")
        val friendUserId: EntityID<UUID>,
        @param:BsonProperty("friend_since")
        @field:BsonProperty("friend_since")
        val friendSince: DateTime
)