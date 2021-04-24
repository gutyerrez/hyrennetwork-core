package com.redefantasy.core.shared.misc.servers.configuration.storage.dto

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
class FetchServerConfigurationByServerNameDTO(
	val serverName: EntityID<String>
)
