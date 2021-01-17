package com.redefantasy.core.shared.users.preferences.storage.dto

import com.redefantasy.core.shared.users.preferences.data.UserPreference
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserPreferenceDTO(
        val userId: UUID,
        val userPreference: UserPreference
)