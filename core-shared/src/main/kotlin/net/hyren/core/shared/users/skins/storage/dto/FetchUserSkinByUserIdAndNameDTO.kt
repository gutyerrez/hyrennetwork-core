package net.hyren.core.shared.users.skins.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FetchUserSkinByUserIdAndNameDTO(
	val userId: EntityID<UUID>,
	val name: String
)