package net.hyren.core.shared.servers.storage.dto

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
class FetchServerByNameDTO(
    val name: EntityID<String>?
)