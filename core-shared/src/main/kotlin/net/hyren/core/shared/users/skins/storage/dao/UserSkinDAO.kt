package net.hyren.core.shared.users.skins.storage.dao

import net.hyren.core.shared.misc.skin.Skin
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntityClass
import net.hyren.core.shared.users.skins.data.UserSkin
import net.hyren.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class UserSkinDAO(
	id: EntityID<String>
) : StringEntity(id) {

	companion object : StringEntityClass<UserSkinDAO>(UsersSkinsTable)

	var name by UsersSkinsTable.id
	var userId by UsersSkinsTable.userId
	var value by UsersSkinsTable.value
	var signature by UsersSkinsTable.signature
	var enabled by UsersSkinsTable.enabled
	var updatedAt by UsersSkinsTable.updatedAt

	fun toUserSkin() = UserSkin(
		name.value,
		userId,
		Skin(
			value,
			signature
		),
		enabled,
		updatedAt
	)

}