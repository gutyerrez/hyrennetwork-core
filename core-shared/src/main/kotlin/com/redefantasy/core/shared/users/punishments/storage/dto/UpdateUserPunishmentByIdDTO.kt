package com.redefantasy.core.shared.users.punishments.storage.dto

import com.redefantasy.core.shared.users.punishments.storage.dao.UserPunishmentDAO
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
class UpdateUserPunishmentByIdDTO(
        val id: Int,
        val execute: Consumer<UserPunishmentDAO>
)