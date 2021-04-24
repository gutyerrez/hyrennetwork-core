package com.redefantasy.core.shared.misc.servers.configuration.data

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.servers.configuration.settings.ServerSettings
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
data class ServerConfiguration<T>(
	val serverName: EntityID<String>,
	val settings: ServerSettings,
	val icon: T? = null
) {

	val server = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(this.serverName)

}
