package net.hyren.core.shared.applications.data

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
data class Application(
    val name: String,
    val displayName: String,
    val slots: Int? = null,
    val address: InetSocketAddress,
    val applicationType: ApplicationType,
    val server: Server? = null,
    val restrictJoin: Group? = null
) {

    fun getFancyDisplayName() = when (applicationType) {
        ApplicationType.LOBBY -> "S. ${name.split("-")[1]}"
        else -> displayName
    }

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Application

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = this.name.hashCode()

}