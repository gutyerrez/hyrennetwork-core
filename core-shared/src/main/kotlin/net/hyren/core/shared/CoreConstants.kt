package net.hyren.core.shared

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.cooldowns.CooldownManager
import okhttp3.OkHttpClient
import org.joda.time.DateTimeZone
import java.util.*

/**
 * @author SrGutyerrez
 **/
object CoreConstants {

	const val HOME_FOLDER = "/home"
	const val MAX_LOGIN_ATTEMPTS = 3

	val CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

	val OK_HTTP = OkHttpClient()
	val RANDOM = Random()
	val DATE_TIME_ZONE = DateTimeZone.forID("America/Sao_Paulo")

	val COOLDOWNS = CooldownManager()

	val WHITELISTED_USERS = listOf(
		"Gutyerrez",
		"ImRamon",
		"CONSOLE",
		"VICTORBBBBR"
	)

	val UN_LOGGED_ALLOWED_COMMANDS = listOf(
		"/logar",
		"/registrar",
		"/login",
		"/register"
	)

	object Info {

		// name

		const val SERVER_NAME = "Rede Fantasy"
		const val COLORED_SERVER_NAME = "§6§lREDE FANTASY"
		const val ERROR_SERVER_NAME = "§c§lREDE FANTASY"

		// url

		const val DISCORD_URL = "discord.redefantasy.com"
		const val SHOP_URL = "loja.redefantasy.com"

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