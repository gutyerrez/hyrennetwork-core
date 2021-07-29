package net.hyren.core.spigot.world.generator

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import java.util.*

class VoidChunkGenerator : ChunkGenerator() {

    override fun getDefaultPopulators(
        world: World
    ) = emptyList<BlockPopulator>()

    override fun generate(
        world: World,
        random: Random,
        x: Int,
        z: Int
    ) = ByteArray(32768)

    /*
    override fun generateChunkData(
        world: World,
        random: Random,
        x: Int,
        z: Int,
        biome: BiomeGrid
    ): ChunkData {
        println("Opa")

        val chunkData = this.createChunkData(world)

        for (x in 0 until 64) {
            for (z in 0 until 64) {
                biome.setBiome(x, z, Biome.JUNGLE)
            }
        }

        chunkData.setBlock(0, 75, 0, Material.BEDROCK)

        return chunkData
    }
     */
    override fun getFixedSpawnLocation(
        world: World,
        random: Random
    ) = Location(world, 0.0, 75.0, 0.0)

    override fun canSpawn(
        world: World,
        x: Int,
        z: Int
    ) = true

}