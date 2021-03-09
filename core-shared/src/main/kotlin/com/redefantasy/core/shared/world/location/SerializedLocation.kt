package com.redefantasy.core.shared.world.location

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.misc.utils.NumberUtils
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

/**
 * @author SrGutyerrez
 **/
data class SerializedLocation @BsonCreator constructor(
    @param:BsonProperty("application_name")
    @field:BsonProperty("application_name")
    val applicationName: String,
    @param:BsonProperty("world_name")
    @field:BsonProperty("world_name")
    val worldName: String = "world",
    @BsonProperty
    val x: Double,
    @BsonProperty
    val y: Double,
    @BsonProperty
    val z: Double,
    @BsonProperty
    val yaw: Float,
    @BsonProperty
    val pitch: Float
) : Document() {

    constructor(
        application: Application,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) : this(application.name, worldName, x, y, z, yaw, pitch)

    constructor(
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) : this(CoreProvider.application.name, worldName, x, y, z, yaw, pitch)

    companion object {

        @JvmStatic
        fun of(string: String?): SerializedLocation? {
            if (string == null) return null

            return CoreConstants.JACKSON.readValue(
                string,
                SerializedLocation::class.java
            )
        }

    }

    fun getBlockX() = NumberUtils.floorInt(this.x)

    fun getBlockY() = NumberUtils.floorInt(this.y)

    fun getBlockZ() = NumberUtils.floorInt(this.z)

    fun getApplication() = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(this.applicationName)

    fun <U : LocationParser<T>, T> parser(parser: U) = parser.apply(this)

    fun clone(): SerializedLocation {
        return SerializedLocation(
            this.applicationName,
            this.worldName,
            this.x,
            this.y,
            this.z,
            this.yaw,
            this.pitch
        )
    }

    override fun toString() = CoreConstants.JACKSON.writeValueAsString(this)

}