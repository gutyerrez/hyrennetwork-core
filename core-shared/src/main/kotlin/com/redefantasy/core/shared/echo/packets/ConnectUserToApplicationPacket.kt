package com.redefantasy.core.shared.echo.packets

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class ConnectUserToApplicationPacket(
    var userId: EntityID<UUID>? = null,
    var application: Application? = null
) : EchoPacket() {

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEntityID(this.userId)
        buffer.writeApplication(this.application)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readEntityID(UsersTable)
        this.application = buffer.readApplication()
    }

}