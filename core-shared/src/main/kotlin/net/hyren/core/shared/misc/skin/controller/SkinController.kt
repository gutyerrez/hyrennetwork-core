package net.hyren.core.shared.misc.skin.controller

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.misc.skin.Skin
import okhttp3.Request

/**
 * @author Gutyerrez
 */
object SkinController {

	private const val API_END_POINT = "https://api.mojang.com"
	private const val SESSION_END_POINT = "https://sessionserver.mojang.com"

	fun fetchSkinByName(name: String): Skin? {
		val request = Request.Builder()
			.url("$API_END_POINT/users/profiles/minecraft/$name")
			.header("Content-Type", "application/json")
			.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20100101 Firefox/10.0")
			.get()
			.build()

		val response = CoreConstants.OK_HTTP.newCall(request)
			.execute()
			.body ?: return null

		val bytes = response.bytes()

		if (bytes.isEmpty()) return null

		val minecraftProfile = CoreConstants.JACKSON.readValue(
			bytes,
			MinecraftProfile::class.java
		)

		val skin: () -> Skin? = invoker@{
			val request = Request.Builder()
				.url("$SESSION_END_POINT/session/minecraft/profile/${minecraftProfile.id}?unsigned=false")
				.header("Content-Type", "application/json")
				.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20100101 Firefox/10.0")
				.get()
				.build()

			val response = CoreConstants.OK_HTTP.newCall(request)
				.execute()
				.body ?: return@invoker null

			val bytes = response.bytes()

			if (bytes.isEmpty()) return@invoker null

			val minecraftProfileData = CoreConstants.JACKSON.readValue(
				bytes,
				MinecraftProfileData::class.java
			)

			val properties = minecraftProfileData.properties[0]

			Skin(
				properties.value,
				properties.signature
			)
		}

		return skin.invoke()
	}

	internal open class MinecraftProfile(
		val id: String,
		val name: String
	)

	internal class MinecraftProfileData(
		id: String,
		name: String,
		val properties: Array<MinecraftProfileDataProperties>
	) : MinecraftProfile(
		id,
		name
	)

	internal data class MinecraftProfileDataProperties(
		val name: String,
		val value: String,
		val signature: String
	)

}

