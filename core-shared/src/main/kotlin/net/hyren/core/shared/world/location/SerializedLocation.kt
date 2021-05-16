package net.hyren.core.shared.world.location

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.misc.json.KJson
import net.hyren.core.shared.misc.utils.NumberUtils

/**
 * @author SrGutyerrez
 **/
data class SerializedLocation(
	val applicationName: String? = null,
	val worldName: String = "world",
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
		fun of(string: String?) = string?.let {
			KJson.decodeFromString(SerializedLocation::class, it)
		} as? SerializedLocation

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

	override fun toString() = KJson.encodeToString(this)

}