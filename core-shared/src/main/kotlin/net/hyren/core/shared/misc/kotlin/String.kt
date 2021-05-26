package net.hyren.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
// Number regex

internal val INT_PATTERN = Regex("^-?[0-9]")
internal val DOUBLE_PATTERN = Regex("^-?(?:0|[1-9][0-9]*)?\\.?[0-9]+([e|E][+-]?[0-9]+)?\$")
internal val LONG_PATTERN = Regex("^-?\\d{1,19}$")
internal val FLOAT_PATTERN = Regex("[+-]?([0-9]*[.])?[0-9]+?([f|F])?")

// Boolean regex

internal val BOOLEAN_PATTERN = Regex("(true|false|sim|s|si|não|nao|na|n|activate|ativar|deactivate|desativar)")

fun String.isInt() = INT_PATTERN.matches(this)

fun String.isDouble() = DOUBLE_PATTERN.matches(this)

fun String.isFloat() = FLOAT_PATTERN.matches(this)

fun String.isLong() = LONG_PATTERN.matches(this)

fun String.isBoolean() = BOOLEAN_PATTERN.matches(this)