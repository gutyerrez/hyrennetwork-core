package com.redefantasy.core.bungee.misc.punish.packets.listeners

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.UserPunishedPacket
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class UserPunishedEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: UserPunishedPacket
    ) {
        val id = packet.id
        val userId = packet.userId
        val message = packet.message

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId!!)

        user?.validatePunishments()

        val proxiedPlayer = ProxyServer.getInstance().getPlayer(userId)

        CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().invalidate(userId)

        if (proxiedPlayer !== null) {
            CoreProvider.Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY.provide().update(
                UpdateUserPunishmentByIdDTO(
                    id!!
                ) {
                    it.startTime = DateTime.now(
                        CoreConstants.DATE_TIME_ZONE
                    )
                }
            )

            val message = user?.validatePunishments()

            println("AA")

            if (message !== null) {
                println("BB")

                proxiedPlayer.disconnect(*message)
            }
        }

        ProxyServer.getInstance().broadcast(*message!!)
    }

}