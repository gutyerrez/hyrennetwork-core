package com.redefantasy.core.shared.users.punishments.storage.dto

import com.redefantasy.core.shared.misc.punish.PunishType
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class CreateUserPunishmentDTO(
        val userId: EntityID<UUID>,
        val stafferId: EntityID<UUID>,
        val punishType: PunishType,
        val punishCategory: EntityID<String>? = null,
        val duration: Long,
        val customReason: String? = null,
        val proof: String? = null,
        val hidden: Boolean = false
)