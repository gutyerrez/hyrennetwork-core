package com.redefantasy.core.bungee

import com.redefantasy.core.bungee.misc.connection.listener.InitialLoginListener
import com.redefantasy.core.bungee.misc.plugin.CustomPlugin
import com.redefantasy.core.bungee.wrapper.BungeeWrapper
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.api.ProxyServer

/**
 * @author Gutyerrez
 */
class CoreBungeePlugin : CustomPlugin(true) {

    override fun onEnable() {
        super.onEnable()

        CoreWrapper.WRAPPER = BungeeWrapper()

        val pluginManager = ProxyServer.getInstance().pluginManager

        pluginManager.registerListener(
            this,
            InitialLoginListener()
        )
    }

}