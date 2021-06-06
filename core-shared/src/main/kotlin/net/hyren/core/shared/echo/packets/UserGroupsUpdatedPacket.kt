package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.echo.api.buffer.*
import net.hyren.core.shared.echo.api.packets.EchoPacket
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class UserGroupsUpdatedPacket(
    var userId: EntityID<UUID>? = null
) : EchoPacket() {

    override fun write(
        buffer: EchoBufferOutput
    ) {
        buffer.writeEntityID(this.userId)
    }

    override fun read(
        buffer: EchoBufferInput
    ) {
        this.userId = buffer.readEntityID()
    }

}