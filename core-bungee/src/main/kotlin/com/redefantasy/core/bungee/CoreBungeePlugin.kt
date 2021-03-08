package com.redefantasy.core.bungee

import com.redefantasy.core.bungee.misc.plugin.CustomPlugin
import com.redefantasy.core.bungee.misc.server.connector.ServerConnector
import com.redefantasy.core.bungee.wrapper.BungeeWrapper
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.BungeeCordConstants

/**
 * @author Gutyerrez
 */
class CoreBungeePlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreWrapper.WRAPPER = BungeeWrapper()

        BungeeCordConstants.SERVER_CONNECTOR = ServerConnector()

        CoreProvider.Cache.Redis.USERS_LOGGED.provide().delete(
            CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByProxyApplication(
                CoreProvider.application
            )
        )
        CoreProvider.Cache.Redis.USERS_STATUS.provide().delete(
            CoreProvider.application
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