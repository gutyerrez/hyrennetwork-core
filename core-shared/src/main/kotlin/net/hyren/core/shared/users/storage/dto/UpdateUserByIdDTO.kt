package net.hyren.core.shared.users.storage.dto

import net.hyren.core.shared.users.storage.dao.UserDAO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserByIdDTO(
        val id: EntityID<UUID>,
        val execute: UserDAO.() -> Unit
)