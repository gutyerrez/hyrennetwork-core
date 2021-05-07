package net.hyren.core.shared.echo.api

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput

/**
 * @author SrGutyerrez
 **/
interface IByteSerializable {

    fun write(buffer: EchoBufferOutput)

    fun read(buffer: EchoBufferInput)

}