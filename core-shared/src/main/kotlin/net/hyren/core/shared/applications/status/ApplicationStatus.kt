package net.hyren.core.shared.applications.status

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.misc.kotlin.InetSocketAddressSerializer
import net.hyren.core.shared.misc.kotlin.ServerSerializer
import net.hyren.core.shared.servers.data.Server
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
@Serializable
open class ApplicationStatus(
    @SerialName("application_name")
    @SerializedName("application_name")
    val applicationName: String,
    @SerialName("application_type")
    @SerializedName("application_type")
    val applicationType: ApplicationType,
    @Serializable(ServerSerializer::class) val server: Server?,
    @Serializable(InetSocketAddressSerializer::class) val address: InetSocketAddress,
    @SerialName("online_since")
    @SerializedName("online_since")
    val onlineSince: Long
) {

    @SerialName("heap_size")
    @SerializedName("heap_size")
    var heapSize: Long? = null
    @SerialName("heap_max_size")
    @SerializedName("heap_max_size")
    var heapMaxSize: Long? = null
    @SerialName("heap_free_size")
    @SerializedName("heap_free_size")
    var heapFreeSize: Long? = null
    @Required
    @SerialName("online_players")
    @SerializedName("online_players")
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