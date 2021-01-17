package com.redefantasy.core.shared.users.friends.data

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class UserFriend @BsonCreator constructor(
        @param:BsonProperty("user_id")
        @field:BsonProperty("user_id")
        val userId: UUID,
        @param:BsonProperty("friend_user_id")
        @field:BsonProperty("friend_user_id")
        val friendUserId: UUID,
        @param:BsonProperty("friend_since")
        @field:BsonProperty("friend_since")
        val friendSince: Long
)