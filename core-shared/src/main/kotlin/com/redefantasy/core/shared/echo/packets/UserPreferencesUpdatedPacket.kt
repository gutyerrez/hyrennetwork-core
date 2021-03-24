package com.redefantasy.core.shared.echo.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.misc.preferences.Preference
import com.redefantasy.core.shared.users.storage.table.UsersTable
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

        val a = buffer.readArray<Any>()

        println(a)

//        this.preferences = a
    }

}