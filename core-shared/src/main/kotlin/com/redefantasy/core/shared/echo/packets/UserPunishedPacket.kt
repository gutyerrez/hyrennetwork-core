package com.redefantasy.core.shared.echo.packets

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.packets.annotations.ServerPacket
import com.redefantasy.core.shared.misc.punish.PunishType
import java.util.*

/**
 * @author Gutyerrez
 */
@ServerPacket
class UserPunishedPacket : EchoPacket() {

    /**
     * redis.getResource().hset(slaoq, aaasad)
     *
     * try (Jedis jedis = redis.getResource()) {
     *  // executa
     * } catch (JedisDataException e) {
     *  e.printStackTrace();
     * }
     *
     */

    var userId: UUID? = null
    var stafferId: UUID? = null
    var punishCategoryName: String? = null
    var punishType: PunishType? = null
    var punishDuration: Long = 0
    var proof: String? = null
    var hidden: Boolean = true

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
        punishType = buffer.readEnum(PunishType::class)!!
        proof = buffer.readString()
        hidden = buffer.readBoolean()
    }

}