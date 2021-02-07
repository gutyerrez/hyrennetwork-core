package com.redefantasy.core.bungee

import com.redefantasy.core.bungee.misc.plugin.CustomPlugin
import com.redefantasy.core.bungee.misc.punish.command.PunishCommand
import com.redefantasy.core.bungee.misc.punish.packets.listeners.UserPunishedPacketListener
import com.redefantasy.core.bungee.misc.server.connector.ServerConnector
import com.redefantasy.core.bungee.wrapper.BungeeWrapper
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.BungeeCordConstants
import net.md_5.bungee.api.ProxyServer

/**
 * @author Gutyerrez
 */
class CoreBungeePlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreWrapper.WRAPPER = BungeeWrapper()

        BungeeCordConstants.SERVER_CONNECTOR = ServerConnector()

        val pluginManager = ProxyServer.getInstance().pluginManager

        pluginManager.registerCommand(this, PunishCommand())

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(UserPunishedPacketListener())
    }

}