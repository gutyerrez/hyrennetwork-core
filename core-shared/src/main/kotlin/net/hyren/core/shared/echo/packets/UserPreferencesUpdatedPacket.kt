package net.hyren.core.shared.echo.packets

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class UserPreferencesUpdatedPacket(
    var userId: EntityID<UUID>? = null,
    var preferences: Array<Preference>? = null
) : EchoPacket() {

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeEntityID(userId)
        buffer.writeArray(preferences)
    }

    override fun read(buffer: EchoBufferInput) {
        this.userId = buffer.readEntityID(UsersTable)
        this.preferences = buffer.readArray()
    }

}