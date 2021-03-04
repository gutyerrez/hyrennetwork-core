package com.redefantasy.core.bungee.echo.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.users.storage.table.UsersTable
import net.md_5.bungee.api.chat.BaseComponent
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FriendAcceptedPacket : EchoPacket() {

    var targetUserId: EntityID<UUID>? = null
    var message: Array<BaseComponent>? = null

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEntityID(this.targetUserId)
        buffer.writeBaseComponent(this.message)
    }

    override fun read(buffer: EchoBufferInput) {
        this.targetUserId = buffer.readEntityID(UsersTable)
        this.message = buffer.readBaseComponent()
    }

}