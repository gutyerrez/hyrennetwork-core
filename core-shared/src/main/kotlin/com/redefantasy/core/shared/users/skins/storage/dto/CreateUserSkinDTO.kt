package com.redefantasy.core.shared.users.skins.storage.dto

import com.redefantasy.core.shared.users.skins.data.UserSkin
import java.util.*

/**
 * @author SrGutyerrez
 **/
class CreateUserSkinDTO(
        val userId: UUID,
        val userSkin: UserSkin
)