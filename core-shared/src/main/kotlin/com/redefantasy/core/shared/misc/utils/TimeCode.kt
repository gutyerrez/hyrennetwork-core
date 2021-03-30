package com.redefantasy.core.shared.misc.utils

import org.apache.commons.lang3.ArrayUtils
import java.text.DecimalFormat

/**
 * @author SrGutyerrez
 **/
enum class TimeCode(
        val milliseconds: Long,
        val single: String,
        val plural: String,
        val aliases: Array<String>
) {

    YEAR(12 * 30 * 24 * 60 * 60 * 1000L, "ano", "anos", arrayOf("a")),
    MONTH(30 * 24 * 60 * 60 * 1000L, "mês", "meses", arrayOf("m")),
    WEEK(7 * 24 * 60 * 60 * 1000L, "semana", "semanas", arrayOf("s")),
    DAY(24 * 60 * 60 * 1000L, "dia", "dias", arrayOf("d")),
    HOUR(60 * 60 * 1000L, "hora", "horas", arrayOf("h")),
    MINUTE(60 * 1000L, "minuto", "minutos", arrayOf("m")),
    SECOND(1000L, "segundo", "segundos", arrayOf("s"));

    companion object {

        fun getUnit(alias: String): TimeCode? {
            var unit: TimeCode? = null

            values().forEach {
                if (ArrayUtils.contains(it.aliases, alias)) unit = it

                if (it.single === alias || it.plural === alias) unit = it
            }

            return unit
        }

        fun fromText(text: String): Long {
            if (text.contains(" ")) {
                val parts = text.split(" ")
                var duration = 0L

                parts.forEach {
                    duration += this.fromText(it)
                }

                return duration
            } else if (text.contains(", ")) {
                val parts = text.split(", ")
                var duration = 0L

                parts.forEach {
                    duration += this.fromText(it)
                }

                return duration
            } else {
                val number = Regex("[^0-9]").replace(text, "").toInt()
                val duration = Regex("[^a-zA-Z]").replace(text, "")

                val timeCode = this.getUnit(duration) ?: SECOND

                return timeCode.milliseconds * number
            }
        }

        fun toText(time: Long, length: Int, zeroDefaultMessage: String? = "Permanente"): String {
            if (time <= 0L && zeroDefaultMessage !== null) return zeroDefaultMessage

            var remainingTime = time
            val builder = StringBuilder()

            var current = 0

            values().forEach {
                if (current != length) {
                    val amount = (remainingTime / it.milliseconds).toInt()

                    if (amount > 0) {
                        val name = if (amount == 1) it.single else it.plural
                        val text = if (builder.isEmpty()) {
                            "$amount $name"
                        } else ", $amount $name"

                        builder.append(text)

                        current++
                    } else {
                        val decimalFormat = DecimalFormat("#.#")

                        val halfSeconds = time / 1000.0

                        builder.append(decimalFormat.format(halfSeconds)).append(it.single)

                        current++
                    }

                    remainingTime -= amount * it.milliseconds
                }
            }

            current = 0

            do {
                val char = builder[current]

                if (char == ',') builder.replace(current, current + 1, " e")

                current++
            } while (builder.length < current)

            return builder.toString()
        }

    }

}