package com.redefantasy.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
inline fun <reified T> Array<T>.copyFrom(copyFromArray: Array<T>) {
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

    _tempArray.forEachIndexed { index, it ->
        this.fill(
            it,
            index,
            index
        )
    }
}

inline fun <reified T> sizedArray(
    size: Int
): Array<T> = java.lang.reflect.Array.newInstance(
    T::class.java.componentType,
    size
) as Array<T>