package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.wrapper.CoreWrapper
import com.redefantasy.core.spigot.echo.packets.listener.SoundEchoPacketListener
import com.redefantasy.core.spigot.echo.packets.listener.TitleEchoPacketListener
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin
import com.redefantasy.core.spigot.wrapper.SpigotWrapper

/**
 * @author Gutyerrez
 */
class CoreSpigotPlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreWrapper.WRAPPER = SpigotWrapper()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TitleEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(SoundEchoPacketListener())
    }

}