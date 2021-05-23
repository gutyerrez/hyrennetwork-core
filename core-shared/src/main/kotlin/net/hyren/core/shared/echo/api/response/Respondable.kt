package net.hyren.core.shared.echo.api.response

/**
 * @author SrGutyerrez
 **/
interface Respondable<T: Response> {

    var response: T

}