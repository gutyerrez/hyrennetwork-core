package com.redefantasy.core.shared.users.preferences.data

import com.redefantasy.core.shared.misc.preferences.data.Preference
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UserPreference(
    val userId: UUID,
    val preference: Preference,
    val status: Boolean
)