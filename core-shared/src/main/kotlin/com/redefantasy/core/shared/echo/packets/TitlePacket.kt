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
    var fadeIn: Long = 0
    var fadeOut: Long = 30
    var stay: Long = 0

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(this.userId)
        buffer.writeString(this.title)
        buffer.writeString(this.subTitle)
        buffer.writeLong(this.fadeIn)
        buffer.writeLong(this.fadeOut)
        buffer.writeLong(this.stay)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readUUID()
        this.title = buffer.readString()
        this.subTitle = buffer.readString()
        this.fadeIn = buffer.readLong()
        this.fadeOut = buffer.readLong()
        this.stay = buffer.readLong()
    }

}