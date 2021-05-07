package net.hyren.core.shared.users.passwords.data

import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
data class UserPassword(
    val id: EntityID<Int>,
    val userId: EntityID<UUID>,
    val password: String,
    var enabled: Boolean,
    val createdAt: DateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as UserPassword

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        return password.hashCode() + id.hashCode() + userId.hashCode()
    }

}
