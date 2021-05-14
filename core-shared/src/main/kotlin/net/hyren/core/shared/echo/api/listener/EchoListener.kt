package net.hyren.core.shared.echo.api.listener

/**
 * @author SrGutyerrez
 **/
interface EchoPacketListener

@Deprecated("EchoListener is unused", ReplaceWith("EchoPacketListener"))
interface EchoListener : EchoPacketListener