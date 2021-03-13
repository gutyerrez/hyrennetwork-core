package com.redefantasy.core.bungee

import com.redefantasy.core.bungee.echo.packet.listener.ConnectUserToApplicationEchoPacketListener
import com.redefantasy.core.bungee.misc.plugin.CustomPlugin
import com.redefantasy.core.bungee.misc.server.connector.ServerConnector
import com.redefantasy.core.bungee.wrapper.BungeeWrapper
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.scheduler.AsyncScheduler
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.table.UsersTable
import com.redefantasy.core.shared.wrapper.CoreWrapper
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

        BungeeCordConstants.SERVER_CONNECTOR = ServerConnector()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(
            ConnectUserToApplicationEchoPacketListener()
        )

        CoreProvider.Cache.Redis.USERS_LOGGED.provide().delete(
            CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByProxyApplication(
                CoreProvider.application
            )
        )
        CoreProvider.Cache.Redis.USERS_STATUS.provide().delete(
            CoreProvider.application
        )

        AsyncScheduler.scheduleAsyncRepeatingTask(
            {
                println("Vai atualizar")

                ProxyServer.getInstance().players.forEach {
                    println("Atualizando ${it.name}...")

                    var user = CoreProvider.Cache.Local.USERS.provide().fetchById(it.uniqueId)

                    if (user === null) user = User(
                        EntityID(
                            it.uniqueId,
                            UsersTable
                        ),
                        it.name
                    )

                    CoreProvider.Cache.Redis.USERS_STATUS.provide().create(
                        user,
                        CoreProvider.application,
                        it.pendingConnection.version
                    )
                }
            },
            0,
            20,
            TimeUnit.SECONDS
        )
    }

    override fun onDisable() {
        super.onDisable()

        CoreProvider.Cache.Redis.USERS_LOGGED.provide().delete(
            CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByProxyApplication(
                CoreProvider.application
            )
        )
        CoreProvider.Cache.Redis.USERS_STATUS.provide().delete(
            CoreProvider.application
        )
    }

}