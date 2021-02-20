package com.redefantasy.core.shared.echo.packets.teleport.state

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.response.Response

/**
 * @author Gutyerrez
 */
class TeleportToApplicationAndUserStatePacket : Response() {

    var teleportState: TeleportState? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEnum(this.teleportState)
    }

    override fun read(buffer: EchoBufferInput) {
        this.teleportState = buffer.readEnum(TeleportState::class)
    }

    enum class TeleportState {

        REQUESTING, TELEPORTING, TELEPORTED

    }

}