package net.hyren.core.shared.misc.utils

/**
 * @author Gutyerrez
 */
class SequencePrefix : AbstractIterator<String>() {

    private var now = -1
    private var prefix = ""

    private var charArray: CharArray = CharArray('Z' - 'A' + 1)

    init {
        var i = 'A'

        while (i <= 'Z') {
            charArray[i - 'A'] = i
            i++
        }
    }

    private fun fixPrefix(prefix: String): String {
        if (prefix.isEmpty()) {
            return charArray[0].toString()
        }

        val last = prefix.length - 1
        val next = (prefix[last].code + 1).toChar()
        val prefix = prefix.substring(0, last)

        return if (next - charArray[0] == this.charArray.size) {
            fixPrefix(prefix) + charArray[0]
        } else {
            prefix + next
        }
    }

    override fun computeNext() {
        if (++now == this.charArray.size) {
            prefix = fixPrefix(prefix)
        }

        now %= this.charArray.size

        setNext(StringBuilder().append(prefix).append(charArray[now]).toString())
    }

}