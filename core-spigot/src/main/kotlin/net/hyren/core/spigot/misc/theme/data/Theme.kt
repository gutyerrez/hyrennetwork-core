package net.hyren.core.spigot.misc.theme.data

import kotlin.experimental.and
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.core.spigot.misc.asNMSWorld
import net.hyren.core.spigot.misc.theme.nbt.ByteArrayTag
import net.hyren.core.spigot.misc.theme.nbt.CompoundTag
import net.hyren.core.spigot.misc.theme.nbt.ShortTag
import net.hyren.core.spigot.misc.theme.nbt.stream.NBTInputStream
import net.minecraft.server.v1_8_R3.Block
import net.minecraft.server.v1_8_R3.ChunkSection
import org.bukkit.Bukkit
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

            var addId = ByteArray(0)

            val blocksIds = sizedArray<Short>(blocks.size)

            if (schematic.containsKey("AddBlocks")) {
                addId = (schematic["AddBlocks"] as ByteArrayTag).value
            }

            for (index in blocks.indices) {
                if ((index shr 1) >= addId.size) {
                    blocksIds[index] = (blocks[index] and 0xFF.toByte()).toShort()
                } else {
                    if ((index and 1) == 0) {
                        blocksIds[index] = (((addId[index shr 1] and 0x0F.toByte()).toInt() shl 8) + (blocks[index] and 0xFF.toByte())).toShort()
                    } else {
                        blocksIds[index] = (((addId[index shr 1] and 0xF0.toByte()).toInt() shl 4) + (blocks[index] and 0xFF.toByte())).toShort()
                    }
                }
            }

            val worldServer = Bukkit.getWorld(worldName).asNMSWorld()

            for (blockX in 0 until width) {
                for (blockY in 0 until height) {
                    for (blockZ in 0 until length) {
                        val chunk = worldServer.getChunkAt(x shr 4, z shr 4)
                        var chunkSection = chunk.sections[y shr 4]

                        if (chunkSection == null) {
                            chunkSection = ChunkSection(
                                y shr 4 shl 4,
                                !chunk.world.worldProvider.o()
                            )

                            chunk.sections[y shr 4] = chunkSection
                        }

                        val index = blockY * width * length + blockZ * width + blockX

                        if (blocksIds[index].toInt() == 0) {
                            continue
                        }

                        val blockData = Block.getByCombinedId(blocksIds[index].toInt() + (data[index].toInt() shl 12))

                        if (!worldServer.chunkProviderServer.isChunkLoaded(blockX, blockZ)) {
                            worldServer.chunkProviderServer.loadChunk(blockX, blockZ)
                        }

                        val x = x - 157
                        val y = y - 59
                        val z = z - 42

                        chunkSection.setType(
                            (x + blockX),
                            (y + blockY),
                            (z + blockZ),
                            blockData
                        )

                        chunkSection.recalcBlockCounts()

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
