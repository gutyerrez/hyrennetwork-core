package com.redefantasy.core.shared.users.skins.data

import com.redefantasy.core.shared.misc.skin.Skin
import org.joda.time.DateTime

/**
 * @author SrGutyerrez
 **/
data class UserSkin(
    val id: Int,
    val name: String,
    val skin: Skin,
    val updatedAt: DateTime
)