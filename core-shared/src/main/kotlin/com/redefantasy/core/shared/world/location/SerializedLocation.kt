package com.redefantasy.core.shared.world.location

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.misc.utils.NumberUtils

/**
 * @author SrGutyerrez
 **/
class SerializedLocation(
    @JsonProperty
    val _id: String,
    @JsonProperty("application_name")
    val applicationName: String,
    @JsonProperty("world_name")
    val worldName: String = "world",
    @JsonProperty
    val x: Double,
    @JsonProperty
    val y: Double,
    @JsonProperty
    val z: Double,
    @JsonProperty
    val yaw: Float,
    @JsonProperty
    val pitch: Float
) {

    constructor(
        application: Application,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) : this("", application.name, worldName, x, y, z, yaw, pitch)

    constructor(
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) : this("", CoreProvider.application.name, worldName, x, y, z, yaw, pitch)

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
            this._id,
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