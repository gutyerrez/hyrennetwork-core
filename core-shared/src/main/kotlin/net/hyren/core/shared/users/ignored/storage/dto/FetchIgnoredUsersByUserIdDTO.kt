package net.hyren.core.shared.users.ignored.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchIgnoredUsersByUserIdDTO(
        val userId: EntityID<UUID>
)