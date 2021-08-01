package net.hyren.core.spigot.misc.theme.data

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.spigot.misc.utils.schematic.Clipboard
import net.hyren.core.spigot.misc.utils.schematic.ClipboardTools
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.File

/**
 * @author Gutyerrez
 */
data class Theme(
    val schematicName: String = "default.schematic"
) {

    private lateinit var schematic: File

    fun load() {
        val schematic = File(
            "${CoreProvider.application.getThemesFolder()}/$schematicName"
        )

        if (!schematic.exists()) {
            throw RuntimeException("Specified theme does not exists.")
        }

        this.schematic = schematic
    }

    fun paste(
        worldName: String = "world",
        x: Int,
        y: Int,
        z: Int
    ) {
        val clipboard = Clipboard(schematic)

        ClipboardTools.binaryPaste(
            Location(
                Bukkit.getWorld(worldName),
                x.toDouble(), y.toDouble(), z.toDouble()
            ),
            clipboard.blockIds,
            clipboard.blockData,
            clipboard.length.toInt(),
            clipboard.width.toInt(),
            clipboard.height.toInt()
        )
    }

    private fun Application.getThemesFolder(): String = when (applicationType) {
        ApplicationType.LOBBY -> "${CoreConstants.THEMES_FOLDER}/lobby"
        ApplicationType.SERVER_SPAWN, ApplicationType.SERVER_VIP -> "${CoreConstants.THEMES_FOLDER}/${name}"
        else -> throw RuntimeException("This application has not theme folder declared")
    }

}
