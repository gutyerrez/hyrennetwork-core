package net.hyren.core.shared.users.punishments.storage.dto

import net.hyren.core.shared.users.punishments.storage.dao.UserPunishmentDAO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
class UpdateUserPunishmentByIdDTO(
        val id: EntityID<Int>,
        val execute: Consumer<UserPunishmentDAO>
)