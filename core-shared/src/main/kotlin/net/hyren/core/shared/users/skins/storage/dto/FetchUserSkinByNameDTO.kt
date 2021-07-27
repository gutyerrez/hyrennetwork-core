package net.hyren.core.shared.users.skins.storage.dto

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class FetchUserSkinByNameDTO(
	val name: EntityID<String>
)