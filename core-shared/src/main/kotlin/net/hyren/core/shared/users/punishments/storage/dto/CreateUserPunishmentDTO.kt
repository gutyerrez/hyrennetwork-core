package net.hyren.core.shared.users.punishments.storage.dto

import net.hyren.core.shared.misc.punish.PunishType
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
    val perpetual: Boolean = false,
    val hidden: Boolean = false
)