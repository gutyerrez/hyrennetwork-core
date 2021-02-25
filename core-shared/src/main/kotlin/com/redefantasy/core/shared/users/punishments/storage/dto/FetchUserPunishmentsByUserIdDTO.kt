package com.redefantasy.core.shared.users.punishments.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserPunishmentsByUserIdDTO(
        val userId: EntityID<UUID>
)