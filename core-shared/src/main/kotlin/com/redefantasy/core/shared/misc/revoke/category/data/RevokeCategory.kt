package com.redefantasy.core.shared.misc.revoke.category.data

import com.redefantasy.core.shared.groups.Group

/**
 * @author SrGutyerrez
 **/
data class RevokeCategory(
        val name: String,
        val displayName: String,
        private val description: String,
        val group: Group,
        val enabled: Boolean
) {

    fun getDescription(): String {
        val builder = StringBuilder()

        if (this.description.contains("\n")) {
            this.description.split("\n").forEach {
                builder.append(it)
                        .append("\n")
            }
        } else builder.append(this.description)

        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as RevokeCategory

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = this.name.hashCode()

}