package net.hyren.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
inline fun <reified T> sizedArray(
    size: Int
): Array<T> = java.lang.reflect.Array.newInstance(
    T::class.java,
    size
) as Array<T>

inline fun <reified T> Array<T>.copyFrom(copyFromArray: Array<T>): Array<T> {
    val _tempArray = java.lang.reflect.Array.newInstance(
        copyFromArray::class.java.componentType,
        copyFromArray.size
    ) as Array<T>

    copyFromArray.forEachIndexed { index, it ->
        val find = this.find { from -> from == it }

        if (find !== null) {
            _tempArray[index] = find
        } else _tempArray[index] = it
    }

    return _tempArray
}
