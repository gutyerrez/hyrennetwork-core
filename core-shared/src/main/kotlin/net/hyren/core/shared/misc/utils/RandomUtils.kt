package net.hyren.core.shared.misc.utils

import net.hyren.core.shared.CoreConstants
import kotlin.math.abs
import kotlin.math.min

/**
 * @author SrGutyerrez
 **/
object RandomUtils {

    fun <T> randomKey(list: List<T>) = list[CoreConstants.RANDOM.nextInt(list.size)]

    fun <T> randomKey(arguments: Array<T>) = arguments[CoreConstants.RANDOM.nextInt(arguments.size)]

    fun randomInt(start: Int, end: Int): Int {
        return CoreConstants.RANDOM.nextInt(abs(start - end) + 1) + min(start, end)
    }

}