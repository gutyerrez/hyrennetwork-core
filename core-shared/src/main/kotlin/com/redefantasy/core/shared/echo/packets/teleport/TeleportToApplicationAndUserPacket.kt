package com.redefantasy.core.shared.echo.packets.teleport

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.response.Respondable
import com.redefantasy.core.shared.echo.packets.teleport.state.TeleportToApplicationAndUserStatePacket
import java.util.*

/**
 * @author Gutyerrez
 */
class TeleportToApplicationAndUserPacket : EchoPacket(), Respondable<TeleportToApplicationAndUserStatePacket> {

    var userId: UUID? = null
    var targetUserId: UUID? = null

    private var _response: TeleportToApplicationAndUserStatePacket? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.userId)
        buffer.writeUUID(this.targetUserId)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readUUID()
        this.targetUserId = buffer.readUUID()
    }

    override fun getResponse() = this._response

    override fun setResponse(t: TeleportToApplicationAndUserStatePacket?) {
        this._response = t
    }

}