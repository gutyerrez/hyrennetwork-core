package net.hyren.core.shared.misc.report.category.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.misc.kotlin.EntityIDSerializer
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
@Serializable
data class ReportCategory(
    @Serializable(EntityIDSerializer::class)
    @SerialName("name") val name: EntityID<String>,
    @SerialName("display_name") val displayName: String,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as ReportCategory

        if (name.value != other.name.value) return false

        return true
    }
}