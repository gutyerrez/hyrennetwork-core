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

    override fun getFixedSpawnLocation(
        world: World,
        random: Random
    ) = Location(world, 0.0, 3.0, 0.0)

    override fun canSpawn(
        world: World,
        x: Int,
        z: Int
    ) = true

}