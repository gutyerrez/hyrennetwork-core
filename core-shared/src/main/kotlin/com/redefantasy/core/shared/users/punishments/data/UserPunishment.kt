package com.redefantasy.core.shared.users.punishments.data

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.punish.PunishType
import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory
import com.redefantasy.core.shared.misc.revoke.category.data.RevokeCategory
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.users.data.User
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
data class UserPunishment(
    val id: EntityID<Int>,
    val userId: EntityID<UUID>,
    val stafferId: EntityID<UUID>,
    var startTime: DateTime? = null,
    val punishType: PunishType,
    val punishCategory: PunishCategory? = null,
    val duration: Long,
    val customReason: String? = null,
    val proof: String? = null,
    var revokeStafferId: EntityID<UUID>? = null,
    var revokeTime: DateTime? = null,
    var revokeCategory: RevokeCategory? = null,
    val hidden: Boolean = false,
    val perpetual: Boolean = false,
    val createdAt: DateTime = DateTime.now(),
    val updatedAt: DateTime? = null
) {

    fun getColor(): ChatColor {
        if (this.startTime === null) {
            if (this.revokeTime !== null) return ChatColor.GRAY

            return ChatColor.YELLOW
        }

        return if (this.startTime!!.withMillis(this.duration).isBefore(System.currentTimeMillis())) {
            ChatColor.GREEN
        } else ChatColor.RED
    }

    fun isBan(): Boolean {
        return this.punishType !== PunishType.MUTE
    }

    fun isActive(): Boolean {
        if (this.startTime === null) {
            println("Start time é nulo")

            if (this.revokeTime !== null) return false

            println("Não foi revogado")

            return true
        }

        println("Verificar aqui")

        val result = this.startTime!!.withMillis(this.duration).isBefore(System.currentTimeMillis())

        println("Resultado: ${if (result) "ativo" else "não está ativo"}")

        return result
    }

    fun canBeRevokedFrom(revoker: User): Boolean {
        if (revoker.hasGroup(Group.MASTER) || revoker.hasGroup(Group.DIRECTOR)) {
            return true
        } else if (revoker.hasGroup(Group.MANAGER)) {
            return this.createdAt + TimeUnit.DAYS.toMillis(7) > DateTime.now()
        } else if (revoker.hasGroup(Group.ADMINISTRATOR)) {
            return this.createdAt + TimeUnit.DAYS.toMillis(3) > DateTime.now()
        } else if (revoker.hasGroup(Group.MODERATOR)) {
            return this.createdAt + TimeUnit.HOURS.toMillis(12) > DateTime.now()
        } else if (revoker.hasGroup(Group.MANAGER)) {
            return this.createdAt + TimeUnit.HOURS.toMillis(2) > DateTime.now()
        }

        return false
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