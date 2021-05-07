package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import java.util.*

/**
 * @author Gutyerrez
 */
class StaffMessagePacket : EchoPacket() {

    var stafferId: UUID? = null
    var bukkitApplication: Application? = null
    var message: String? = null

    override fun read(buffer: EchoBufferInput) {
        this.stafferId = buffer.readUUID()
        this.bukkitApplication = buffer.readApplication()
        this.message = buffer.readString()
    }

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.stafferId)
        buffer.writeApplication(this.bukkitApplication)
        buffer.writeString(this.message)
    }

}