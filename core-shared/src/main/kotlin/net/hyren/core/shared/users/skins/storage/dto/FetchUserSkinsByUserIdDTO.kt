package net.hyren.core.shared.users.skins.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class FetchUserSkinsByUserIdDTO(
	val userId: EntityID<UUID>
)