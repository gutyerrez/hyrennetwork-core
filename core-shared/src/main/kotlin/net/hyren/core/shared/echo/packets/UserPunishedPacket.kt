package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.echo.api.buffer.*
import net.hyren.core.shared.echo.api.packets.EchoPacket
import net.hyren.core.shared.echo.api.packets.annotations.ServerPacket
import net.md_5.bungee.api.chat.BaseComponent
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
@ServerPacket
class UserPunishedPacket : EchoPacket() {

    var id: EntityID<Int>? = null
    var userId: UUID? = null
    var message: Array<BaseComponent>? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEntityID(this.id)
        buffer.writeUUID(this.userId)
        buffer.writeBaseComponent(this.message)
    }

    override fun read(buffer: EchoBufferInput) {
        this.id = buffer.readEntityID()
        this.userId = buffer.readUUID()
        this.message = buffer.readBaseComponent()
    }

}