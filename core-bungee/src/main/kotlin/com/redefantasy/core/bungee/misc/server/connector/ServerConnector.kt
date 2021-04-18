package com.redefantasy.core.bungee.misc.server.connector

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.misc.preferences.PREMIUM_ACCOUNT
import com.redefantasy.core.shared.misc.preferences.PreferenceState
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.table.UsersTable
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.connection.ServerConnector
import org.jetbrains.exposed.dao.id.EntityID
import java.net.InetSocketAddress
import java.util.*

/**
 * @author Gutyerrez
 */
class ServerConnector : ServerConnector {

	override fun fetchLobbyServer(userId: UUID?): InetSocketAddress? {
		if (userId === null) return null

		val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

		return if (user?.getPreferences()?.find { it == PREMIUM_ACCOUNT }?.preferenceState == PreferenceState.ENABLED) {
			CoreConstants.fetchLobbyApplication()?.address
		} else {
			CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(ApplicationType.LOGIN)
				.stream()
				.sorted { application1, application2 ->
					val applicationStatus1 =
						CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
							application1,
							ApplicationStatus::class
						)
					val applicationStatus2 =
						CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
							application2,
							ApplicationStatus::class
						)

					if (applicationStatus1 === null || applicationStatus2 === null) return@sorted 0

					if (applicationStatus1.onlinePlayers < application1.slots ?: 0 && applicationStatus2.onlinePlayers < application2.slots ?: 0)
						return@sorted applicationStatus2.onlinePlayers.compareTo(applicationStatus1.onlinePlayers)

					return@sorted 0
				}.findFirst().orElse(null)?.address
		}
	}

	override fun updateAndGetNext(
		proxiedPlayer: ProxiedPlayer,
		inetSocketAddress: InetSocketAddress
	): InetSocketAddress? {
		val application = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByAddress(
			inetSocketAddress
		) ?: return null

		if (arrayOf(
				ApplicationType.LOGIN,
				ApplicationType.LOBBY,
				ApplicationType.PUNISHED_LOBBY
			).contains(application.applicationType)
		) return null

		return CoreConstants.fetchLobbyApplication()?.address
	}

	override fun changedUserApplication(
		proxiedPlayer: ProxiedPlayer,
		bukkitApplicationAddress: InetSocketAddress
	) {
		val bukkitApplication = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByAddress(
			bukkitApplicationAddress
		)

		if (bukkitApplication === null) {
			val disconnectMessage = ComponentBuilder()
				.append("§c§lREDE FANTASY")
				.append("\n\n")
				.append("§cNão foi possível localizar a aplicação.")
				.create()

			proxiedPlayer.disconnect(*disconnectMessage)
			return
		}

		var user = CoreProvider.Cache.Local.USERS.provide().fetchById(proxiedPlayer.uniqueId)

		if (user === null) user = User(
			EntityID(proxiedPlayer.uniqueId, UsersTable),
			proxiedPlayer.name
		)

		CoreProvider.Cache.Redis.USERS_STATUS.provide().create(
			user,
			bukkitApplication,
			proxiedPlayer.pendingConnection.version
		)
	}

}