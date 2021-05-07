package net.hyren.core.shared.applications.packets

import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket

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