package net.hyren.core.spigot.misc.server.configuration.storage.dto

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
class FetchServerConfigurationByServerNameDTO(
	val serverName: EntityID<String>
)
