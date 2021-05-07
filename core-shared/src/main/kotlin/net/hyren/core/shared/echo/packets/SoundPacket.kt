package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import net.hyren.core.shared.misc.utils.Sound
import java.util.*
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class SoundPacket : EchoPacket() {

    var sound: Sound? = null
    var volume1: Float? = null
    var volume2: Float? = null
    var usersId: List<UUID>? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEnum(this.sound)
        buffer.writeFloat(this.volume1)
        buffer.writeFloat(this.volume2)
        buffer.writeList(this.usersId)
    }

    override fun read(buffer: EchoBufferInput) {
        this.sound = buffer.readEnum(Sound::class)
        this.volume1 = buffer.readFloat()
        this.volume2 = buffer.readFloat()
        this.usersId = buffer.readList<String>()
            ?.stream()
            ?.map { UUID.fromString(it) }
            ?.collect(Collectors.toList())
    }

}