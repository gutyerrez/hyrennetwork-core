package net.hyren.core.shared.users.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserByIdDTO(
        val id: EntityID<UUID>
)