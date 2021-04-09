package com.redefantasy.core.shared.users.skins.storage.dao

import com.redefantasy.core.shared.misc.skin.Skin
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class UserSkinDAO(
	id: EntityID<Int>
) : IntEntity(id) {

	companion object : IntEntityClass<UserSkinDAO>(UsersSkinsTable)

	var name by UsersSkinsTable.name
	var userId by UsersSkinsTable.userId
	var signature by UsersSkinsTable.signature
	var value by UsersSkinsTable.value
	var enabled by UsersSkinsTable.enabled
	var updatedAt by UsersSkinsTable.updatedAt

	fun toUserSkin() = UserSkin(
		this.name,
		this.userId,
		Skin(
			this.value,
			this.signature
		),
		this.enabled,
		this.updatedAt
	)

}