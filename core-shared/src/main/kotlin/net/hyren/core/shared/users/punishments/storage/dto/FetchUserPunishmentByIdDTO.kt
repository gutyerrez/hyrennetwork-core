package net.hyren.core.shared.users.punishments.storage.dto

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class FetchUserPunishmentByIdDTO(
        val id: EntityID<Int>
)