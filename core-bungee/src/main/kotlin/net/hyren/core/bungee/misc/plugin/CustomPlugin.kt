package net.hyren.core.bungee.misc.plugin

import net.hyren.core.shared.CoreProvider
import net.md_5.bungee.BungeeCordConstants
import net.md_5.bungee.api.plugin.Plugin
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
abstract class CustomPlugin(
    private val prepareProviders: Boolean = false
) : Plugin() {

    override fun onEnable() {
        if (prepareProviders) {
            try {
                CoreProvider.prepare(
                    (BungeeCordConstants.LISTENER_INFO.socketAddress as InetSocketAddress).port
                )
                val echo = CoreProvider.Databases.Redis.ECHO.provide()

                echo.defaultSubscriber = echo.subscribe { _, runnable ->
                    runnable.run()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDisable() {
        CoreProvider.shutdown()
    }

}