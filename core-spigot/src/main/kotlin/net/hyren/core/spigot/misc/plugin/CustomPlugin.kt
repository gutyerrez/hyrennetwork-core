package net.hyren.core.spigot.misc.plugin

import net.hyren.core.shared.CoreProvider
import net.minecraft.server.v1_8_R3.DedicatedServer
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Gutyerrez
 */
abstract class CustomPlugin(
    private val prepareProviders: Boolean = false
) : JavaPlugin() {

    override fun onEnable() {
        if (prepareProviders) {
            CoreProvider.prepare(Bukkit.getServer().port)

            val echo = CoreProvider.Databases.Redis.ECHO.provide()

            echo.defaultSubscriber = echo.subscribe { _, runnable ->
                runnable.run()
            }
        }
    }

    fun getDefaultWorld(): World {
        return Bukkit.getWorld(
            DedicatedServer.getServer().propertyManager.getString("level-name", "world")
        )
    }

}