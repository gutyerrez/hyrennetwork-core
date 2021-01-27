package com.redefantasy.core.bungee.misc.plugin

import com.redefantasy.core.shared.CoreProvider
import net.md_5.bungee.BungeeCordConstants
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.misc.settings.Settings
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
abstract class CustomPlugin(
    private val prepareProviders: Boolean = false
) : Plugin() {

    override fun onEnable() {
        if (prepareProviders) {
            CoreProvider.prepare(
                (BungeeCordConstants.LISTENER_INFO.socketAddress as InetSocketAddress).port
            )
        }
    }

}