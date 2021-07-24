package net.hyren.core.bungee

import net.hyren.core.bungee.echo.packet.listener.ConnectUserToApplicationEchoPacketListener
import net.hyren.core.bungee.listeners.connection.PreLoginListener
import net.hyren.core.bungee.misc.plugin.CustomPlugin
import net.hyren.core.bungee.misc.server.connector.BungeeServerConnector
import net.hyren.core.bungee.wrapper.BungeeWrapper
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.scheduler.AsyncScheduler
import net.hyren.core.shared.users.data.User
import net.hyren.core.shared.users.storage.table.UsersTable
import net.hyren.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.BungeeCordConstants
import net.md_5.bungee.api.ProxyServer
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class CoreBungeePlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreWrapper.WRAPPER = BungeeWrapper()

        BungeeCordConstants.SERVER_CONNECTOR = BungeeServerConnector()

        val pluginManager = ProxyServer.getInstance().pluginManager

        pluginManager.registerListener(this, PreLoginListener())

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(
            ConnectUserToApplicationEchoPacketListener()
        )

        AsyncScheduler.scheduleAsyncRepeatingTask(
            {
                ProxyServer.getInstance().players.forEach {
                    var user = CoreProvider.Cache.Local.USERS.provide().fetchById(it.uniqueId)

                    if (user === null) user = User(
                        EntityID(
                            it.uniqueId,
                            UsersTable
                        ),
                        it.name
                    )

                    val bukkitApplication = user.getConnectedBukkitApplication()

                    CoreProvider.Cache.Redis.USERS_STATUS.provide().create(
                        user,
                        bukkitApplication,
                        it.pendingConnection.version
                    )
                }
            },
            0,
            1,
            TimeUnit.SECONDS
        )
    }

}