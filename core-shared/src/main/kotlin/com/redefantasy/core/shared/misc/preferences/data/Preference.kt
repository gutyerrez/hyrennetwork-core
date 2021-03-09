package com.redefantasy.core.shared.misc.preferences.data

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonProperty

/**
 * @author SrGutyerrez
 **/
abstract class Preference @BsonCreator constructor(
        @BsonProperty
        val name: String,
        @BsonIgnore val description: String
)