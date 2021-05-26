package net.hyren.core.shared.misc.utils

import net.hyren.core.shared.misc.kotlin.isInt
import net.hyren.core.shared.misc.kotlin.isLong
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

/**
 * @author SrGutyerrez
 **/
object NumberUtils {

    private val ROMAN_MAP = TreeMap<Int, String>()

    init {
        ROMAN_MAP[1000] = "M"
        ROMAN_MAP[900] = "DM"
        ROMAN_MAP[500] = "D"
        ROMAN_MAP[400] = "CD"
        ROMAN_MAP[100] = "C"
        ROMAN_MAP[90] = "XC"
        ROMAN_MAP[50] = "L"
        ROMAN_MAP[40] = "XL"
        ROMAN_MAP[10] = "X"
        ROMAN_MAP[9] ="IX"
        ROMAN_MAP[5] ="V"
        ROMAN_MAP[4] ="IV"
        ROMAN_MAP[1] ="I"
    }

    fun floorInt(number: Double): Int {
        val floor = number.toInt()

        return if (floor.toDouble() == number) {
            floor
        } else {
            floor - (number.toRawBits() ushr 63).toInt()
        }
    }

    fun formatToRoman(number: Int): String {
        val floorKey = ROMAN_MAP.floorKey(number)

        return if (number == floorKey) {
            ROMAN_MAP[number]!!
        } else {
            ROMAN_MAP[floorKey] + formatToRoman(number - floorKey)
        }
    }

    fun formatToK(value: Double): String {
        if (value < 1000) {
            return String.format("%.2f", value)
        }

        val exp = (ln(value) / ln(1000.0)).toInt()

        return String.format(
                "%.0f%c",
                value / 1000.0.pow(exp),
                "KMBTPE"[exp - 1]
        )
    }

    @Deprecated(
        "Cannot use this function to validate int numbers",
        ReplaceWith(
            "isInt()",
            "net.hyren.core.shared.misc.kotlin.StringKt.isInt"
        )
    )
    fun isValidInteger(string: String) = string.isInt()

    @Deprecated(
        "Cannot use this function to validate long numbers",
        ReplaceWith(
            "isLong()",
            "net.hyren.core.shared.misc.kotlin.StringKt.isLong"
        )
    )
    fun isValidLong(string: String) = string.isLong()
}