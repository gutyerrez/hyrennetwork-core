package net.hyren.core.shared.misc.report.category.data

import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
data class ReportCategory(
        val name: EntityID<String>,
        val displayName: String,
        private val description: String,
        val enabled: Boolean
) {

    fun getDescription(): String {
        val builder = StringBuilder()

        if (this.description.contains("\\\n")) {
            val descriptions = this.description.split("\\n")

            descriptions.forEachIndexed { index, text ->
                builder.append(text)

                if (index + 1 < descriptions.size) builder.append("\n")
            }
        } else builder.append(this.description)

        return builder.toString()
    }

    override fun hashCode() = this.name.hashCode()

}