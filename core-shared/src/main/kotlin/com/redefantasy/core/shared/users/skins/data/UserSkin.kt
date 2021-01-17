package com.redefantasy.core.shared.users.skins.data

import com.redefantasy.core.shared.misc.skin.Skin
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonProperty
import org.joda.time.DateTime

/**
 * @author SrGutyerrez
 **/
data class UserSkin @BsonCreator constructor(
        @param:BsonProperty("id")
        @field:BsonProperty("id")
        val id: Int,
        @BsonIgnore
        val name: String,
        @param:BsonProperty("skin")
        @field:BsonProperty("skin")
        val skin: Skin,
        @BsonIgnore
        val updatedAt: DateTime
)