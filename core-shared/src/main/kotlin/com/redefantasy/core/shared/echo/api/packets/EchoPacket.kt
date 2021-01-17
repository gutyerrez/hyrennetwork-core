package com.redefantasy.core.shared.echo.api.packets

import com.redefantasy.core.shared.echo.api.IByteSerializable

/**
 * @author SrGutyerrez
 **/
abstract class EchoPacket : IByteSerializable {

    open var packetHeader: EchoPacketHeader? = null

}