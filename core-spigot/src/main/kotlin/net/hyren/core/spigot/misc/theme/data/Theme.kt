package net.hyren.core.spigot.misc.theme.data

import kotlin.experimental.and
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.spigot.misc.asNMSWorld
import net.minecraft.server.v1_8_R3.Block
import net.minecraft.server.v1_8_R3.BlockPosition
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools
import org.bukkit.Bukkit
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
        FileInputStream(schematic).use {
            val nbtTagCompound = NBTCompressedStreamTools.a(it)

            val width = nbtTagCompound.getShort("Width")
            val height = nbtTagCompound.getShort("Height")
            val length = nbtTagCompound.getShort("Length")

            val blocks = nbtTagCompound.getByteArray("Blocks")
            val data = nbtTagCompound.getByteArray("Data")

            var addBlocks = byteArrayOf(0)

            if (nbtTagCompound.hasKey("AddBlocks")) {
                addBlocks = nbtTagCompound.getByteArray("AddBlocks")
            }

            val placeBlocks = ByteArray(blocks.size)

            blocks.forEachIndexed { index, byte ->
                if ((index shr 1) >= addBlocks.size) {
                    placeBlocks[index] = (byte and 0xFF.toByte())
                } else {
                    if ((index and 1) == 0) {
                        placeBlocks[index] = (((addBlocks[index shr 1] and 0x0F).toInt() shl 8) + (byte and 0xFF.toByte())).toByte()
                    } else {
                        placeBlocks[index] = (((addBlocks[index shr 1] and 0x0F).toInt() shl 4) + (byte and 0xFF.toByte())).toByte()
                    }
                }
            }

            var index = 0

            for (pasteX in 0..width) {
                for (pasteY in 0..height) {
                    for (pasteZ in 0..length) {
                        val blockPosition = BlockPosition(
                            pasteX,
                            y + pasteY,
                            pasteZ
                        )

                        val blockData = Block.getByCombinedId(placeBlocks[pasteX].toInt() + (data[pasteZ].toInt() shl 12))

                        Bukkit.getWorld(worldName).asNMSWorld().setTypeAndData(
                            blockPosition,
                            blockData,
                            0
                        )

                        index++
                    }
                }
            }
        }
    }

    private fun Application.getThemesFolder(): String = when (applicationType) {
        ApplicationType.LOBBY -> "${CoreConstants.THEMES_FOLDER}/lobby"
        ApplicationType.SERVER_SPAWN, ApplicationType.SERVER_VIP -> "${CoreConstants.THEMES_FOLDER}/${name}"
        else -> throw RuntimeException("This application has not theme folder declared")
    }

}
