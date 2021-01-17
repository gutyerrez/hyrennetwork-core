package com.redefantasy.core.shared.users.ignored.data

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class UserIgnored @BsonCreator constructor(
        @param:BsonProperty("user_id")
        @field:BsonProperty("user_id")
        val userId: UUID,
        @param:BsonProperty("ignored_user_id")
        @field:BsonProperty("ignored_user_id")
        val friendUserId: UUID,
        @param:BsonProperty("ignored_since")
        @field:BsonProperty("ignored_since")
        val friendSince: Long
)