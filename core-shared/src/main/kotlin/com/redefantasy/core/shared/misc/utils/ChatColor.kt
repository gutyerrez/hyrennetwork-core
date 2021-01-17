package com.redefantasy.core.shared.misc.utils

import java.awt.Color
import java.util.*
import java.util.regex.Pattern


/**
 * @author SrGutyerrez
 **/
class ChatColor(
        val code: Char,
        val name: String,
        val color: Color?
) {

    companion object {

        val COLOR_CHAR = '\u00A7'
        val ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx"
        val STRIP_COLOR_PATTERN: Pattern = Pattern.compile("(?i)$COLOR_CHAR[0-9A-FK-ORX]")

        private val BY_CHAR: MutableMap<Char, ChatColor> = HashMap()
        private val BY_NAME: MutableMap<String, ChatColor> = HashMap()

        val BLACK: ChatColor = ChatColor('0', "black", Color(0x000000))

        val DARK_BLUE: ChatColor = ChatColor('1', "dark_blue", Color(0x0000AA))

        val DARK_GREEN: ChatColor = ChatColor('2', "dark_green", Color(0x00AA00))

        val DARK_AQUA: ChatColor = ChatColor('3', "dark_aqua", Color(0x00AAAA))

        val DARK_RED: ChatColor = ChatColor('4', "dark_red", Color(0xAA0000))

        val DARK_PURPLE: ChatColor = ChatColor('5', "dark_purple", Color(0xAA00AA))

        val GOLD: ChatColor = ChatColor('6', "gold", Color(0xFFAA00))

        val GRAY: ChatColor = ChatColor('7', "gray", Color(0xAAAAAA))

        val DARK_GRAY: ChatColor = ChatColor('8', "dark_gray", Color(0x555555))

        val BLUE: ChatColor = ChatColor('9', "blue", Color(0x5555FF))

        val GREEN: ChatColor = ChatColor('a', "green", Color(0x55FF55))

        val AQUA: ChatColor = ChatColor('b', "aqua", Color(0x55FFFF))

        val RED: ChatColor = ChatColor('c', "red", Color(0xFF5555))

        val LIGHT_PURPLE: ChatColor = ChatColor('d', "light_purple", Color(0xFF55FF))

        val YELLOW: ChatColor = ChatColor('e', "yellow", Color(0xFFFF55))

        val WHITE: ChatColor = ChatColor('f', "white", Color(0xFFFFFF))

        val MAGIC: ChatColor = ChatColor('k', "obfuscated", null)

        val BOLD: ChatColor = ChatColor('l', "bold", null)

        val STRIKETHROUGH: ChatColor = ChatColor('m', "strikethrough", null)

        val UNDERLINE: ChatColor = ChatColor('n', "underline", null)

        val ITALIC: ChatColor = ChatColor('o', "italic", null)

        val RESET: ChatColor = ChatColor('r', "reset", null)

        fun stripColor(input: String?): String {
            return if (input == null) {
                ""
            } else STRIP_COLOR_PATTERN.matcher(input).replaceAll("")
        }

        fun translateAlternateColorCodes(altColorChar: Char, textToTranslate: String): String {
            val chars = textToTranslate.toCharArray()

            for (i in 0 until chars.size - 1) {
                if (chars[i] == altColorChar && ALL_CODES.indexOf(chars[i + 1]) > -1) {
                    chars[i] = COLOR_CHAR
                    chars[i + 1] = Character.toLowerCase(chars[i + 1])
                }
            }

            return String(chars)
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

}