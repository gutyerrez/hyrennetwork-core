package com.redefantasy.core.spigot.misc.plugin

import com.redefantasy.core.shared.CoreProvider
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Gutyerrez
 */
abstract class CustomPlugin(
    private val prepareProviders: Boolean = false
) : JavaPlugin() {

    override fun onEnable() {
        if (prepareProviders) {
            CoreProvider.prepare(
                Bukkit.getPort()
            )

            val echo = CoreProvider.Databases.Redis.ECHO.provide()

            echo.defaultSubscriber = echo.subscribe { _, runnable ->
                runnable.run()
            }
        }
    }

}