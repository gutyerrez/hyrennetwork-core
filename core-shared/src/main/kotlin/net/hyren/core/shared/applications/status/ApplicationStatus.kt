package net.hyren.core.shared.applications.status

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.servers.data.Server
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
open class ApplicationStatus(
    val applicationName: String,
    val applicationType: ApplicationType,
    val server: Server?,
    val address: InetSocketAddress,
    val onlineSince: Long
) {

    var heapSize: Long? = null
    var heapMaxSize: Long? = null
    var heapFreeSize: Long? = null
    var onlinePlayers: Int = 0

    fun isSame(applicationName: String) = this.applicationName == applicationName

    fun isSameServer(other: ApplicationStatus) = this.server != null && this.server == other.server

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as ApplicationStatus

        if (applicationName != other.applicationName) return false

        return true
    }

    override fun hashCode(): Int {
        return this.applicationName.hashCode()
    }

}