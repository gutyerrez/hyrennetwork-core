package net.hyren.core.shared.echo.api.packets

import net.hyren.core.shared.echo.api.IByteSerializable
import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import java.util.*

/**
 * @author SrGutyerrez
 **/
class EchoPacketHeader(
        var uuid: UUID? = null,
        var senderApplicationName: String? = null,
        var senderServerName: String? = null,
        var responseUUID: UUID? = null
) : IByteSerializable {

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.uuid)
        buffer.writeString(this.senderApplicationName)
        buffer.writeString(this.senderServerName)
        buffer.writeUUID(this.responseUUID)
    }

    override fun read(buffer: EchoBufferInput) {
        this.uuid = buffer.readUUID()
        this.senderApplicationName = buffer.readString()
        this.senderServerName = buffer.readString()
        this.responseUUID = buffer.readUUID()
    }

}