package net.hyren.core.shared.users.storage.dto

import net.hyren.core.shared.users.storage.dao.UserDAO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
class UpdateUserByIdDTO(
        val id: EntityID<UUID>,
        val execute: Consumer<UserDAO>
)