package com.redefantasy.core.shared.echo.api

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput

/**
 * @author SrGutyerrez
 **/
interface IByteSerializable {

    fun write(buffer: EchoBufferOutput)

    fun read(buffer: EchoBufferInput)

}