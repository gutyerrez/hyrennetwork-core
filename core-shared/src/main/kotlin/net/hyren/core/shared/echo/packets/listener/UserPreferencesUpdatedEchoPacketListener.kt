package net.hyren.core.shared.echo.packets.listener

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.packets.UserPreferencesUpdatedPacket
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class UserPreferencesUpdatedEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: UserPreferencesUpdatedPacket
    ) {
        val userId = packet.userId!!
        val preferences = packet.preferences!!

        CoreProvider.Cache.Local.USERS_PREFERENCES.provide().put(
            userId,
            preferences
        )
    }

}