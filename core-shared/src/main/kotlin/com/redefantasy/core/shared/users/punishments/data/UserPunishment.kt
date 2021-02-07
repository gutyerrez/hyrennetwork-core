package com.redefantasy.core.shared.users.punishments.data

import com.redefantasy.core.shared.misc.punish.PunishType
import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory
import com.redefantasy.core.shared.misc.revoke.category.data.RevokeCategory
import com.redefantasy.core.shared.misc.utils.ChatColor
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author SrGutyerrez
 **/
data class UserPunishment(
    val id: EntityID<Int>,
    val userId: EntityID<UUID>,
    val stafferId: EntityID<UUID>,
    val startTime: DateTime? = null,
    val punishType: PunishType,
    val punishCategory: PunishCategory? = null,
    val duration: Long,
    val customReason: String? = null,
    val proof: String? = null,
    val revokeStafferId: EntityID<UUID>? = null,
    val revokeTime: DateTime? = null,
    val revokeCategory: RevokeCategory? = null,
    val hidden: Boolean = false,
    val perpetual: Boolean = false,
    val createdAt: DateTime = DateTime.now(),
    val updatedAt: DateTime? = null
) {

    fun getColor(): ChatColor {
        if (this.startTime == null) {
            if (this.revokeTime != null) return ChatColor.GRAY

            return ChatColor.YELLOW
        }

        return if (this.startTime.millis + this.duration > System.currentTimeMillis()) {
            ChatColor.GREEN
        } else ChatColor.RED
    }

    fun isBan(): Boolean {
        return this.punishType !== PunishType.MUTE
    }

    fun isActive(): Boolean {
        if (this.startTime == null) {
            if (this.revokeTime != null) return false

            return true
        }

        return (this.startTime.millis + this.duration) > System.currentTimeMillis()
    }

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as UserPunishment

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

}