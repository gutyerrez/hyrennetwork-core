package com.redefantasy.core.shared.users.preferences.storage.dto

import com.redefantasy.core.shared.users.preferences.data.UserPreference
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserPreferenceDTO(
        val userId: EntityID<UUID>,
        val userPreference: UserPreference
)