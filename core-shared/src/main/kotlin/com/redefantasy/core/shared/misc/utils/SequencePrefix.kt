package com.redefantasy.core.shared.misc.utils

import com.google.common.collect.AbstractIterator

/**
 * @author Gutyerrez
 */
class SequencePrefix : AbstractIterator<String>() {

    private var now = -1
    private var prefix = ""

    private var vs: CharArray = CharArray('Z' - 'A' + 1)

    init {
        var i = 'A'

        while (i <= 'Z') {
            vs[i - 'A'] = i
            i++
        }
    }

    private fun fixPrefix(prefix: String): String {
        if (prefix.length == 0) {
            return Character.toString(vs.get(0))
        }

        val last = prefix.length - 1
        val next = (prefix[last].toInt() + 1).toChar()
        val sprefix = prefix.substring(0, last)

        return if (next - vs.get(0) == this.vs.size) fixPrefix(sprefix) + vs.get(
            0
        ) else sprefix + next
    }

    override fun computeNext(): String {
        if (++now == this.vs.size) {
            prefix = fixPrefix(prefix)
        }

        now %= this.vs.size

        return StringBuilder().append(prefix).append(vs.get(now)).toString()
    }

}