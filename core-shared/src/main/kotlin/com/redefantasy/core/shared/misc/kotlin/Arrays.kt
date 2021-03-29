package com.redefantasy.core.shared.misc.kotlin

/**
 * @author Gutyerrez
 */
inline fun <reified T> Array<T>.copyFrom(copyFromArray: Array<T>) {
    val _tempArray = java.lang.reflect.Array.newInstance(
        copyFromArray::class.java.componentType,
        copyFromArray.size
    ) as Array<T>

    println(_tempArray.size)

    copyFromArray.forEachIndexed { index, it ->
        val find = this.find { from -> from == it }

        if (find !== null) {
            println("Dale aq $index")
            _tempArray[index] = find

            println("Epa")
        } else {
            println("Vai dale aq $index")

            _tempArray[index] = it

            println("deu lhe")
        }
    }

    _tempArray.forEachIndexed { index, it ->
        this[index] = it
    }
}

public inline fun <reified T> sizedArray(
    size: Int
): Array<T> = java.lang.reflect.Array.newInstance(
    T::class.java.componentType,
    size
) as Array<T>