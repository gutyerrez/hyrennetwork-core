package com.redefantasy.core.shared.echo.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.packets.annotations.ServerPacket
import com.redefantasy.core.shared.users.punishments.storage.table.UsersPunishmentsTable
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
        this.id = buffer.readEntityID(UsersPunishmentsTable)
        this.userId = buffer.readUUID()
        this.message = buffer.readBaseComponent()
    }

}