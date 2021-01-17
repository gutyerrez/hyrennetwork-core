package com.redefantasy.core.shared.echo.api.response

/**
 * @author SrGutyerrez
 **/
interface Respondable<T: Response> {

    fun getResponse(): T?

    fun setResponse(t: T)

}