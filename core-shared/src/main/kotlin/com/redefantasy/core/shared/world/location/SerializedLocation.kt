package com.redefantasy.core.shared.world.location

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.misc.utils.NumberUtils

/**
 * @author SrGutyerrez
 **/
class SerializedLocation {

    @JsonProperty
    var _id: String = ""
    @JsonProperty("application_name")
    var applicationName: String = ""
    @JsonProperty("world_name")
    var worldName: String = "world"
    @JsonProperty
    var x: Double = 0.0
    @JsonProperty
    var y: Double = 0.0
    @JsonProperty
    var z: Double = 0.0
    @JsonProperty
    var yaw: Float = 0F
    @JsonProperty
    var pitch: Float = 0F

    constructor(
        applicationName: String,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) {
        this.applicationName = applicationName
        this.worldName = worldName
        this.x = x
        this.y = y
        this.z = z
        this.yaw = yaw
        this.pitch = pitch
    }

    constructor(
        application: Application,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) {
        this.applicationName = application.name
        this.worldName = worldName
        this.x = x
        this.y = y
        this.z = z
        this.yaw = yaw
        this.pitch = pitch
    }

    constructor(
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) {
        this.worldName = worldName
        this.x = x
        this.y = y
        this.z = z
        this.yaw = yaw
        this.pitch = pitch
    }

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