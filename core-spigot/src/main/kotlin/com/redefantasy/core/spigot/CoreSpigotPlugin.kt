package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.spigot.echo.packets.listener.TitleEchoPacketListener
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin

/**
 * @author Gutyerrez
 */
class CoreSpigotPlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TitleEchoPacketListener())
    }

}