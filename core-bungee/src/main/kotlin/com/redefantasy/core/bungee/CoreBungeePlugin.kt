package com.redefantasy.core.bungee

import com.redefantasy.core.bungee.command.defaults.player.LobbyCommand
import com.redefantasy.core.bungee.command.defaults.player.OnlineCommand
import com.redefantasy.core.bungee.command.defaults.player.ReplyCommand
import com.redefantasy.core.bungee.command.defaults.player.TellCommand
import com.redefantasy.core.bungee.command.defaults.staff.*
import com.redefantasy.core.bungee.command.defaults.staff.account.AccountCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.GroupCommand
import com.redefantasy.core.bungee.command.defaults.staff.server.ServerCommand
import com.redefantasy.core.bungee.echo.packets.listeners.TellPacketListener
import com.redefantasy.core.bungee.misc.login.commands.LoginCommand
import com.redefantasy.core.bungee.misc.login.commands.RegisterCommand
import com.redefantasy.core.bungee.misc.login.listeners.LoginListeners
import com.redefantasy.core.bungee.misc.plugin.CustomPlugin
import com.redefantasy.core.bungee.misc.punish.command.PunishCommand
import com.redefantasy.core.bungee.misc.punish.packets.listeners.UserPunishedEchoPacketListener
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
        pluginManager.registerCommand(this, LoginCommand())
        pluginManager.registerCommand(this, RegisterCommand())
        pluginManager.registerCommand(this, AccountCommand())
        pluginManager.registerCommand(this, GroupCommand())
        pluginManager.registerCommand(this, ServerCommand())
        pluginManager.registerCommand(this, AnnounceCommand())
        pluginManager.registerCommand(this, BTPCommand())
        pluginManager.registerCommand(this, FindCommand())
        pluginManager.registerCommand(this, KickCommand())
        pluginManager.registerCommand(this, SendCommand())
        pluginManager.registerCommand(this, StaffChatCommand())
        pluginManager.registerCommand(this, StaffListCommand())
        pluginManager.registerCommand(this, LobbyCommand())
        pluginManager.registerCommand(this, OnlineCommand())
        pluginManager.registerCommand(this, ReplyCommand())
        pluginManager.registerCommand(this, TellCommand())

        pluginManager.registerListener(this, LoginListeners())

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(UserPunishedEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TellPacketListener())
    }

    override fun onDisable() {
        super.onDisable()

        CoreProvider.Cache.Redis.USERS_STATUS.provide().delete()
        CoreProvider.Cache.Redis.USERS_LOGGED.provide().delete()
    }

}