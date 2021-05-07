package net.hyren.core.shared.providers

/**
 * @author SrGutyerrez
 **/
interface IProvider<T> {

    fun prepare()

    fun provide(): T

    fun shutdown() {
        // shutdown method
    }

}