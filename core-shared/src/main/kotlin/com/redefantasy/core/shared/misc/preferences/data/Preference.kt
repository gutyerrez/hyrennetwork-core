package com.redefantasy.core.shared.misc.preferences.data

import org.bson.codecs.pojo.annotations.BsonIgnore

/**
 * @author SrGutyerrez
 **/
abstract class Preference(
        val name: String,
        @BsonIgnore val description: String
)