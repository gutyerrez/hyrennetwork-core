package net.hyren.core.spigot.command.registry

import net.hyren.core.spigot.command.CustomCommand
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.CraftServer

/**
 * @author Gutyerrez
 */
object CommandRegistry {

    fun registerCommand(customCommand: CustomCommand) {
        val craftServer = Bukkit.getServer() as CraftServer

        craftServer.commandMap.apply {
            register(customCommand.name, customCommand)
        }
    }

}