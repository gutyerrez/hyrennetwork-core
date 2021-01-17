package com.redefantasy.core.shared.misc.report.category.data

/**
 * @author SrGutyerrez
 **/
data class ReportCategory(
        val name: String,
        val displayName: String,
        private val description: String,
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

    override fun hashCode() = this.name.hashCode()

}