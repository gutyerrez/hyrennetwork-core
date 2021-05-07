package net.hyren.core.shared.users.skins.data

import net.hyren.core.shared.misc.skin.Skin
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class UserSkin(
    val name: String,
    val userId: EntityID<UUID>,
    val skin: Skin,
    var enabled: Boolean,
    var updatedAt: DateTime
)