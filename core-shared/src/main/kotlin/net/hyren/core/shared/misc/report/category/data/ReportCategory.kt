package net.hyren.core.shared.misc.report.category.data

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
data class ReportCategory(
    val name: EntityID<String>,
    val displayName: String,
    val _description: String,
    val enabled: Boolean
) {

    fun getDescription(): String {
        val builder = StringBuilder()

        if (this._description.contains("\\\n")) {
            val descriptions = this._description.split("\\n")

            descriptions.forEachIndexed { index, text ->
                builder.append(text)

                if (index + 1 < descriptions.size) builder.append("\n")
            }
        } else builder.append(this._description)

        return builder.toString()
    }

    override fun hashCode() = this.name.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as ReportCategory

        if (name.value != other.name.value) return false

        return true
    }

}