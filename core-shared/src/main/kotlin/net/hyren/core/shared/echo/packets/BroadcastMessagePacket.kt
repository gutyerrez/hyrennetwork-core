package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author Gutyerrez
 */
class BroadcastMessagePacket : EchoPacket() {

    var message: Array<BaseComponent>? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeBaseComponent(this.message)
    }

    override fun read(buffer: EchoBufferInput) {
        this.message = buffer.readBaseComponent()
    }

}