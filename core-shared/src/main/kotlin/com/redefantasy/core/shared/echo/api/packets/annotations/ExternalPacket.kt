package com.redefantasy.core.shared.echo.api.packets.annotations

/**
 * @author SrGutyerrez
 **/
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class ExternalPacket(val channel: String = "")
