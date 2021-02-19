package com.redefantasy.core.shared.misc.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat

/**
 * @author Gutyerrez
 */
object DateFormatter {

    fun formatToDefault(dateTime: DateTime?, joiner: String = ""): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy${if (joiner.isNotEmpty()) " $joiner " else " "}hh:mm")

        if (dateTime === null) return "Data inválida"

        return simpleDateFormat.format(dateTime.millis)
    }

}