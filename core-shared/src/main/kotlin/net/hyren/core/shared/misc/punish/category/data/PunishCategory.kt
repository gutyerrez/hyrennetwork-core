package net.hyren.core.shared.misc.punish.category.data

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.punish.durations.PunishDuration
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
data class PunishCategory(
    val name: EntityID<String>,
    val displayName: String,
    val _description: String,
    val punishDurations: Array<PunishDuration>,
    val group: Group,
    val enabled: Boolean
) {

    fun getName() = this.name.value

    fun getDescription(): String {
        val builder = StringBuilder()

        if (this._description.contains("\\n")) {
            val descriptions = this._description.split("\\n")

            descriptions.forEachIndexed { index, text ->
                builder.append(text)

                if (index + 1 < descriptions.size) builder.append("\n")
            }
        } else builder.append(this._description)

        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as PunishCategory

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = this.name.hashCode()

}