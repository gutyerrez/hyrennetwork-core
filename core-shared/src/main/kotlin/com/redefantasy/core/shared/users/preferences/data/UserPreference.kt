package com.redefantasy.core.shared.users.preferences.data

import com.redefantasy.core.shared.misc.preferences.data.Preference
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

/**
 * @author SrGutyerrez
 **/
open class UserPreference @BsonCreator constructor(
    @param:BsonProperty("user_id")
    @field:BsonProperty("user_id")
    val userId: UUID,
    @BsonProperty
    val preference: Preference,
    @BsonProperty
    val status: Boolean
)