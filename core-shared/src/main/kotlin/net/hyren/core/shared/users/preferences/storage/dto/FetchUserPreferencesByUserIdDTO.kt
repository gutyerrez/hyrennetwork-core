package net.hyren.core.shared.users.preferences.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserPreferencesByUserIdDTO(
        val userId: EntityID<UUID>
)