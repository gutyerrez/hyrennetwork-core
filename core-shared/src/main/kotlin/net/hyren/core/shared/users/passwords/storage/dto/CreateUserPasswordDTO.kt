package net.hyren.core.shared.users.passwords.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class CreateUserPasswordDTO(
    val userId: EntityID<UUID>,
    val password: String
)