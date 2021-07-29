package net.hyren.core.spigot.misc.theme.data

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.spigot.misc.theme.nbt.ByteArrayTag
import net.hyren.core.spigot.misc.theme.nbt.CompoundTag
import net.hyren.core.spigot.misc.theme.nbt.ShortTag
import net.hyren.core.spigot.misc.theme.nbt.stream.NBTInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.DataFormatException
import java.util.zip.GZIPInputStream

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
        FileInputStream(schematic).use {
            val nbtInputStream = NBTInputStream(
                GZIPInputStream(it)
            )

            val schematicTag = nbtInputStream.readTag() as CompoundTag

            nbtInputStream.close()

            if (schematicTag.name != "Schematic") {
                throw DataFormatException("Tag \"Schematic\" does not exists or is not first")
            }

            val schematic = schematicTag.value

            if (!schematic.containsKey("Blocks")) {
                throw DataFormatException("Schematic file is missing a \"Blocks\" tag")
            }

            val width = (schematic["Width"] as ShortTag).value
            val height = (schematic["Height"] as ShortTag).value
            val length = (schematic["Length"] as ShortTag).value

            val blocks = (schematic["Blocks"] as ByteArrayTag).value
            val data = (schematic["Data"] as ByteArrayTag).value

            val placeBlocks = ByteArray(blocks.size)

            println("Bora por (($width/$height) * $length)")
        }
    }

    private fun Application.getThemesFolder(): String = when (applicationType) {
        ApplicationType.LOBBY -> "${CoreConstants.THEMES_FOLDER}/lobby"
        ApplicationType.SERVER_SPAWN, ApplicationType.SERVER_VIP -> "${CoreConstants.THEMES_FOLDER}/${name}"
        else -> throw RuntimeException("This application has not theme folder declared")
    }

}
