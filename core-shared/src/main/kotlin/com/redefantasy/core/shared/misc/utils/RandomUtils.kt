package com.redefantasy.core.shared.misc.utils

import com.redefantasy.core.shared.CoreConstants
import kotlin.math.abs
import kotlin.math.min

/**
 * @author SrGutyerrez
 **/
object RandomUtils {

    fun <T> randomKey(list: List<T>) = list[com.redefantasy.core.shared.CoreConstants.RANDOM.nextInt(list.size)]

    fun <T> randomKey(arguments: Array<T>) = arguments[com.redefantasy.core.shared.CoreConstants.RANDOM.nextInt(arguments.size)]

    fun randomInt(start: Int, end: Int): Int {
        return com.redefantasy.core.shared.CoreConstants.RANDOM.nextInt(abs(start - end) + 1) + min(start, end)
    }

}