package com.redefantasy.core.shared.servers.data

import com.redefantasy.core.shared.servers.ServerType

/**
 * @author SrGutyerrez
 **/
data class Server(
        val name: String,
        val displayName: String,
        val serverType: ServerType
) {

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Server

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = this.name.hashCode()

}