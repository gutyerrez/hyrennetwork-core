package com.redefantasy.core.shared.misc.server.configuration.data

import com.redefantasy.core.shared.misc.server.configuration.settings.ServerSettings

/**
 * @author Gutyerrez
 */
data class ServerConfiguration<T>(
	val settings: ServerSettings,
	val icon: T? = null
)
