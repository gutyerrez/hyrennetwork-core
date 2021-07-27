package net.hyren.core.shared

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.cooldowns.CooldownManager
import net.hyren.core.shared.misc.utils.ChatColor
import okhttp3.OkHttpClient
import org.joda.time.DateTimeZone
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
object CoreConstants {

	const val HOME_FOLDER = "/home"
	const val CLOUD_FOLDER = "${HOME_FOLDER}/cloud"
	const val THEMES_FOLDER = "${CLOUD_FOLDER}/themes"

	val CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

	val OK_HTTP = OkHttpClient.Builder()
		.connectTimeout(15, TimeUnit.SECONDS)
		.readTimeout(15, TimeUnit.SECONDS)
		.writeTimeout(15, TimeUnit.SECONDS)
		.build()

	val RANDOM = Random()
	val DATE_TIME_ZONE = DateTimeZone.getDefault()

	val COOLDOWNS = CooldownManager()

	val WHITELISTED_USERS = listOf(
		"Gutyerrez",
		"joaopedro9990"
	)

	val UN_LOGGED_ALLOWED_COMMANDS = listOf(
		"/logar",
		"/registrar",
		"/login",
		"/register"
	)

	object Info {

		// colors

		val DEFAULT_COLOR = ChatColor.BLUE

		// name

		const val SERVER_NAME = "Hyren"
		const val COLORED_SERVER_NAME = "§9§lHYREN"
		const val ERROR_SERVER_NAME = "§c§lHYREN"

		// url

		const val DISCORD_URL = "hyrgo.me/discord"
		const val SHOP_URL = "https://loja.hyren.net"

	}

	fun fetchLobbyApplication(): Application? = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
		ApplicationType.LOBBY
	).shuffled()
		.stream()
		.filter {
			CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
				it,
				ApplicationStatus::class
			) != null
		}
		.filter {
			CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(it).size < it.slots ?: 0
		}
		.min { application1, application2 ->
			CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(
				application1
			).size.compareTo(
				CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(
					application2
				).size
			)
		}
		.orElse(null)

}