package net.hyren.core.shared.world.location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.misc.utils.NumberUtils

/**
 * @author SrGutyerrez
 **/
@Serializable
data class SerializedLocation(
	@SerialName("application_name") val applicationName: String? = null,
	@SerialName("world_name") val worldName: String = "world",
	val x: Double,
	val y: Double,
	val z: Double,
	val yaw: Float,
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

			return Json.decodeFromString(string)
		}

	}

	fun getBlockX() = NumberUtils.floorInt(this.x)

	fun getBlockY() = NumberUtils.floorInt(this.y)

	fun getBlockZ() = NumberUtils.floorInt(this.z)

	fun getApplication(): Application? = this.applicationName?.let {
		return@let CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(it)
	}

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

	override fun toString() = Json.encodeToString(this)

}