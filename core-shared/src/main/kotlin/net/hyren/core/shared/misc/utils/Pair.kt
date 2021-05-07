package net.hyren.core.shared.misc.utils

/**
 * @author SrGutyerrez
 **/
class Pair<L, R>(
        val left: L,
        val right: R
) {

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as Pair<*, *>

        if (left != other.left || right != other.right) return false

        return true
    }

    override fun hashCode(): Int {
        return left.hashCode() + right.hashCode()
    }

}