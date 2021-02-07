package com.redefantasy.core.bungee.misc.punish.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.misc.punish.PunishType
import java.util.*

/**
 * @author Gutyerrez
 */
class UserPunishedPacket(
    var userId: UUID,
    var stafferId: UUID,
    var punishCategoryName: String,
    var punishType: PunishType,
    var punishDuration: Long,
    var proof: String?,
    var hidden: Boolean
) : EchoPacket() {

    override fun write(buffer: EchoBufferOutput) {
        buffer.writeUUID(userId)
        buffer.writeUUID(stafferId)
        buffer.writeString(punishCategoryName)
        buffer.writeEnum(punishType)
        buffer.writeLong(punishDuration)
        buffer.writeString(proof)
        buffer.writeBoolean(hidden)
    }

    override fun read(buffer: EchoBufferInput) {
        userId = buffer.readUUID()!!
        stafferId = buffer.readUUID()!!
        punishCategoryName = buffer.readString()!!
        punishType = buffer.readEnum(PunishType::class.java)!!
        proof = buffer.readString()
        hidden = buffer.readBoolean()
    }

}