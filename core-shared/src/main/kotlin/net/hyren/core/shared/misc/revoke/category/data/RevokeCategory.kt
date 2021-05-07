package net.hyren.core.shared.misc.revoke.category.data

import net.hyren.core.shared.groups.Group
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
data class RevokeCategory(
    val name: EntityID<String>,
    val displayName: String,
    private val description: String,
    val group: Group,
    val enabled: Boolean
) {

    fun getDescription(): String {
        val builder = StringBuilder()

        if (this.description.contains("\\n")) {
            val descriptions = this.description.split("\\n")

            descriptions.forEachIndexed { index, text ->
                builder.append(text)

                if (index + 1 < descriptions.size) builder.append("\n")
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