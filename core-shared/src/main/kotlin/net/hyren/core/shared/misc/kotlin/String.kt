package net.hyren.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
fun String.isInt() = this.toIntOrNull() != null

fun String.isDouble() = this.toDoubleOrNull() != null

fun String.isFloat() = this.toFloatOrNull() != null

fun String.isLong() = this.toLongOrNull() != null

fun String.isBoolean() = this.toBooleanStrictOrNull() != null