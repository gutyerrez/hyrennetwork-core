package com.redefantasy.core.shared.misc.utils

import java.util.*
import kotlin.math.ln
import kotlin.math.pow

/**
 * @author SrGutyerrez
 **/
object NumberUtils {

    private val ROMAN_MAP = TreeMap<Int, String>()

    init {
        this.ROMAN_MAP[1000] = "M"
        this.ROMAN_MAP[900] = "DM"
        this.ROMAN_MAP[500] = "D"
        this.ROMAN_MAP[400] = "CD"
        this.ROMAN_MAP[100] = "C"
        this.ROMAN_MAP[90] = "XC"
        this.ROMAN_MAP[50] = "L"
        this.ROMAN_MAP[40] = "XL"
        this.ROMAN_MAP[10] = "X"
        this.ROMAN_MAP[9] ="IX"
        this.ROMAN_MAP[5] ="V"
        this.ROMAN_MAP[4] ="IV"
        this.ROMAN_MAP[1] ="I"
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
        val floorKey = this.ROMAN_MAP.floorKey(number)

        return if (number == floorKey) {
            this.ROMAN_MAP[number]!!
        } else {
            this.ROMAN_MAP[floorKey] + this.formatToRoman(number - floorKey)
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

    fun isValidInteger(string: String): Boolean {
        return try {
            string.toInt()

            true
        } catch (ignored: NumberFormatException) {
            false
        }
    }

    fun isValidLong(string: String): Boolean {
        return try {
            string.toLong()

            true
        } catch (ignored: NumberFormatException) {
            false
        }
    }

}