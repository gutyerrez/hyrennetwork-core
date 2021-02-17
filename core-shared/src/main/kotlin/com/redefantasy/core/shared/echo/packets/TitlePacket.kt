package com.redefantasy.core.shared.echo.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.packets.annotations.ServerPacket
import java.util.*

/**
 * @author Gutyerrez
 */
@ServerPacket
class TitlePacket : EchoPacket() {

    var userId: UUID? = null
    var title: String? = null
    var subTitle: String? = null
    var fadeIn: Int? = 0
    var fadeOut: Int? = 30
    var stay: Int? = 0

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.userId)
        buffer.writeString(this.title)
        buffer.writeString(this.subTitle)
        buffer.writeInt(this.fadeIn)
        buffer.writeInt(this.fadeOut)
        buffer.writeInt(this.stay)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readUUID()
        this.title = buffer.readString()
        this.subTitle = buffer.readString()
        this.fadeIn = buffer.readInt()
        this.fadeOut = buffer.readInt()
        this.stay = buffer.readInt()
    }

}