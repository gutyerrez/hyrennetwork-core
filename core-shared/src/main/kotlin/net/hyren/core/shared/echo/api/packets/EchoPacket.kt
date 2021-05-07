package net.hyren.core.shared.echo.api.packets

import net.hyren.core.shared.echo.api.IByteSerializable

/**
 * @author SrGutyerrez
 **/
abstract class EchoPacket : IByteSerializable {

    open var packetHeader: EchoPacketHeader? = null

}