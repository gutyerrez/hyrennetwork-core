package net.hyren.core.spigot.misc.theme.data

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

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
        DataInputStream(
            FileInputStream(schematic)
        ).use {
            val nbtTagCompound = NBTCompressedStreamTools.a(it)

            val width = nbtTagCompound.getShort("Width")
            val height = nbtTagCompound.getShort("Height")
            val length = nbtTagCompound.getShort("Length")

            val blocks = nbtTagCompound.getByteArray("Blocks")
            val data = nbtTagCompound.getByteArray("Data")

            val placeBlocks = ByteArray(blocks.size)

            println("Bora por")
        }
    }

    private fun Application.getThemesFolder(): String = when (applicationType) {
        ApplicationType.LOBBY -> "${CoreConstants.THEMES_FOLDER}/lobby"
        ApplicationType.SERVER_SPAWN, ApplicationType.SERVER_VIP -> "${CoreConstants.THEMES_FOLDER}/${name}"
        else -> throw RuntimeException("This application has not theme folder declared")
    }

}
