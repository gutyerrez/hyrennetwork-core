package net.hyren.core.shared.users.preferences.storage.dto

import net.hyren.core.shared.misc.preferences.data.Preference
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class CreateUserPreferencesDTO(
    val userId: EntityID<UUID>,
    val preferences: Array<Preference>
)