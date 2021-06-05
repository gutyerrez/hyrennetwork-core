package net.hyren.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
fun String.isInt() = this.toIntOrNull() != null

fun String.isDouble() = this.toDoubleOrNull() != null

fun String.isFloat() = this.toFloatOrNull() != null

fun String.isLong() = this.toLongOrNull() != null

fun String.isBoolean() = Regex("(true|false|t|f|sim|não|nao|ñ|n|s|si|yes|no|y|yep|yap)").matches(this)

fun generateString(length: Int = 16): String {
    val source = ('A'..'Z') + ('a'..'z') + (1..9) + '_'

    return (1..length).map { source.random() }.joinToString()
}