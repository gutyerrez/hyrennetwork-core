package net.hyren.core.shared.misc.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat

/**
 * @author Gutyerrez
 */
object DateFormatter {

    fun formatToDefault(dateTime: DateTime?, joiner: String = ""): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")

        if (dateTime === null) return "Data inválida"

        val formatted = simpleDateFormat.format(dateTime.millis)

        return if (joiner.isEmpty()) formatted else {
            formatted.replaceFirst(
                " ",
                " $joiner "
            )
        }
    }

}