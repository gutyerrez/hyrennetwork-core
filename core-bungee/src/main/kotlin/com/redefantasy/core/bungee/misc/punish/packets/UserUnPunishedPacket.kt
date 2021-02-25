package com.redefantasy.core.bungee.misc.punish.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import net.md_5.bungee.api.chat.BaseComponent
import java.util.*

/**
 * @author Gutyerrez
 */
class UserUnPunishedPacket : EchoPacket() {

    var userId: UUID? = null
    var message: Array<BaseComponent>? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.userId)
        buffer.writeBaseComponent(message)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readUUID()
        this.message = buffer.readBaseComponent()
    }

}