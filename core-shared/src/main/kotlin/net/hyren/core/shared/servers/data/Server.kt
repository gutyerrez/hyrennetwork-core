package net.hyren.core.shared.servers.data

import net.hyren.core.shared.servers.ServerType
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
data class Server(
    val name: EntityID<String>,
    val displayName: String,
    val serverType: ServerType
) {

    fun getName() = name.value

    fun getFancyDisplayName() = displayName.replace(
        Regex("(Rank UP|Factions).*"),
        when (serverType) {
            ServerType.FACTIONS -> "F."
            ServerType.RANK_UP -> "R."
        }
    )

    fun isAlphaServer() = Regex("(Alpha)").containsMatchIn(displayName)

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Server

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = name.hashCode()

}
