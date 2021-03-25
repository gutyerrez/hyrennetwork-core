package com.redefantasy.core.shared.users.preferences.storage.dto

import com.redefantasy.core.shared.misc.preferences.data.Preference
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserPreferencesDTO(
    val userId: EntityID<UUID>,
    val preferences: Array<Preference>
)