package com.redefantasy.core.shared.echo.packets.listener

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.UserPreferencesUpdatedPacket
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class UserPreferencesUpdatedEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: UserPreferencesUpdatedPacket
    ) {
        val userId = packet.userId!!
        val preferences = packet.preferences!!

        println(preferences.contentToString())

        CoreProvider.Cache.Local.USERS_PREFERENCES.provide().put(
            userId,
            preferences
        )
    }

}