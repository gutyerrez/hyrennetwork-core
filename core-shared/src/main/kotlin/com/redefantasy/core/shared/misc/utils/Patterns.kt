package com.redefantasy.core.shared.misc.utils

import com.google.common.base.Joiner

/**
 * @author SrGutyerrez
 **/
enum class Patterns(
        val regex: Regex
) {

    SPACE(Regex(" ")),
    VAR_1(Regex("\\$1")),
    VAR_2(Regex("\\$2")),
    VAR_3(Regex("\\$3")),
    VAR_4(Regex("\\$4")),
    VAR_5(Regex("\\$5")),
    NEW_LINE(Regex("\n")),
    DOT(Regex("[.]")),
    SEMI_COLON(Regex(";")),
    COLON(Regex(":")),
    HYPHEN(Regex("-")),
    AT(Regex("@")),
    DOUBLE_SLASH(Regex("//")),
    BUSINESS_E(Regex("&")),
    COLOR_CHARACTER(Regex("§")),
    PASSWORD(Regex("[a-zA-Z0-9_@#$%&*_\\-.]*")),
    NICK(Regex("[a-zA-Z0-9_]{3,16}")),
    EMAIL(Regex("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")),
    EMAIL_MASK(Regex("(^[^@]{3}|(?!^)\\G)[^@]")),
    IP(Regex("^(?:(?:0?0?\\d|0?[1-9]\\d|1\\d\\d|2[0-5][0-5]|2[0-4]\\d)\\.){3}(?:0?0?\\d|0?[1-9]\\d|1\\d\\d|2[0-5][0-5]|2[0-4]\\d)$"));

    private val joiner = Joiner.on(this.regex.pattern)

    fun matches(input: String): Boolean {
        return this.regex.matches(input)
    }

    fun split(input: String): List<String> {
        return this.regex.split(input)
    }

    fun replace(input: String, replacement: String) {
        this.regex.replace(input, replacement)
    }

    fun join(arguments: Collection<String>): String {
        return this.joiner.join(arguments)
    }

    fun join(arguments: Array<String>): String {
        return this.joiner.join(arguments)
    }

}