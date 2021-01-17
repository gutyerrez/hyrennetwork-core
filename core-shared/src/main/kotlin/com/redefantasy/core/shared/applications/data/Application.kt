package com.redefantasy.core.shared.applications.data

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
data class Application(
        val name: String,
        val displayName: String,
        val description: String? = null,
        val slots: Int? = null,
        val address: InetSocketAddress,
        val applicationType: ApplicationType,
        val server: Server? = null,
        val restrictJoin: Group? = null
) {

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