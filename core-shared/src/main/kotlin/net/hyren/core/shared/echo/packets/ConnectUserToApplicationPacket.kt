package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.echo.api.buffer.*
import net.hyren.core.shared.echo.api.packets.EchoPacket
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class ConnectUserToApplicationPacket(
    var userId: EntityID<UUID>? = null,
    var application: Application? = null
) : EchoPacket() {

    override fun write(
        buffer: EchoBufferOutput
    ) {
        buffer.writeEntityID(userId)
        buffer.writeApplication(application)
    }

    override fun read(
        buffer: EchoBufferInput
    ) {
        userId = buffer.readEntityID()
        application = buffer.readApplication()
    }

}