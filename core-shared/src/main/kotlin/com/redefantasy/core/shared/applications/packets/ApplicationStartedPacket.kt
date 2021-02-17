package com.redefantasy.core.shared.applications.packets

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket

/**
 * @author SrGutyerrez
 **/
class ApplicationStartedPacket : EchoPacket() {

    var application: Application? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeApplication(this.application)
    }

    override fun read(buffer: EchoBufferInput) {
        this.application = buffer.readApplication()
    }

}