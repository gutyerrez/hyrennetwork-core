package net.hyren.core.shared.echo.api.packets.annotations

/**
 * @author SrGutyerrez
 **/
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ExternalPacket(val channel: String = "")
