package net.hyren.core.shared.misc.utils

import java.awt.Color
import java.util.*
import java.util.regex.Pattern


/**
 * @author SrGutyerrez
 **/
class ChatColor(
    val code: Char, name: String,
    private val color: Color? = null
) {

    companion object {
        val COLOR_CHAR = '\u00A7'
        val ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx"

        private val BY_CHAR: MutableMap<Char, ChatColor> = HashMap()
        private val BY_NAME: MutableMap<String, ChatColor> = HashMap()

        val STRIP_COLOR_PATTERN = Pattern.compile("(?i)$COLOR_CHAR[0-9A-FK-ORX]")
        val BLACK = ChatColor('0', "black", Color(0x000000))
        val DARK_BLUE = ChatColor('1', "dark_blue", Color(0x0000AA))
        val DARK_GREEN = ChatColor('2', "dark_green", Color(0x00AA00))
        val DARK_AQUA = ChatColor('3', "dark_aqua", Color(0x00AAAA))
        val DARK_RED = ChatColor('4', "dark_red", Color(0xAA0000))
        val DARK_PURPLE = ChatColor('5', "dark_purple", Color(0xAA00AA))
        val GOLD = ChatColor('6', "gold", Color(0xFFAA00))
        val GRAY = ChatColor('7', "gray", Color(0xAAAAAA))
        val DARK_GRAY = ChatColor('8', "dark_gray", Color(0x555555))
        val BLUE = ChatColor('9', "blue", Color(0x5555FF))
        val GREEN = ChatColor('a', "green", Color(0x55FF55))
        val AQUA = ChatColor('b', "aqua", Color(0x55FFFF))
        val RED = ChatColor('c', "red", Color(0xFF5555))
        val LIGHT_PURPLE = ChatColor('d', "light_purple", Color(0xFF55FF))
        val YELLOW = ChatColor('e', "yellow", Color(0xFFFF55))
        val WHITE = ChatColor('f', "white", Color(0xFFFFFF))
        val MAGIC = ChatColor('k', "obfuscated")
        val BOLD = ChatColor('l', "bold")
        val STRIKETHROUGH = ChatColor('m', "strikethrough")
        val UNDERLINE = ChatColor('n', "underline")
        val ITALIC = ChatColor('o', "italic")
        val RESET = ChatColor('r', "reset")

        fun stripColor(input: String?): String? {
            return if (input == null) {
                null
            } else STRIP_COLOR_PATTERN.matcher(input).replaceAll("")
        }

        fun translateAlternateColorCodes(altColorChar: Char, textToTranslate: String): String? {
            val b = textToTranslate.toCharArray()
            for (i in 0 until b.size - 1) {
                if (b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1]) > -1) {
                    b[i] = COLOR_CHAR
                    b[i + 1] = Character.toLowerCase(b[i + 1])
                }
            }
            return String(b)
        }

        fun getByChar(code: Char): ChatColor? {
            return BY_CHAR[code]
        }

        fun getLastColors(input: String): String {
            var result = ""

            val length = input.length

            for (index in length - 1 downTo -1 + 1) {
                val section = input[index]

                if (section == COLOR_CHAR && index < length - 1) {
                    val c = input[index + 1]
                    val color = getByChar(c)

                    if (color != null) {
                        result = color.toString() + result

                        if (color.isColor() || color == RESET) {
                            break
                        }
                    }
                }
            }

            return result
        }

        fun fromHEX(hexCode: String): ChatColor? {
            when (hexCode) {
                "#0000AA" -> return DARK_BLUE
                "#00AA00" -> return DARK_GREEN
                "#00AAAA" -> return DARK_AQUA
                "#AA0000" -> return DARK_RED
                "#AA00AA" -> return DARK_PURPLE
                "#555555" -> return DARK_GRAY
                "#FF55FF" -> return LIGHT_PURPLE
                "#000000" -> return BLACK
                "#FFFFFF" -> return WHITE
                "#FFAA00" -> return GOLD
                "#AAAAAA" -> return GRAY
                "#5555FF" -> return BLUE
                "#55FF55" -> return GREEN
                "#55FFFF" -> return AQUA
                "#FF5555" -> return RED
                "#FFFF55" -> return YELLOW
            }

            return null
        }

    }

    private var count = 0

    private var ordinal = 0

    init {
        ordinal = count++
        BY_CHAR[code] = this
        BY_NAME[name.toUpperCase(Locale.ROOT)] = this
    }

    fun isColor() = arrayOf(
        DARK_BLUE,
        DARK_AQUA,
        DARK_GRAY,
        DARK_GREEN,
        DARK_PURPLE,
        DARK_RED,
        LIGHT_PURPLE,
        BLACK,
        WHITE,
        RED,
        GREEN,
        YELLOW,
        GOLD,
        BLUE,
        AQUA
    ).contains(this)

    override fun toString() = String(
        charArrayOf(
            COLOR_CHAR, code
        )
    )

}