package net.hyren.core.spigot.world

import net.hyren.core.shared.world.location.SerializedLocation
import net.hyren.core.spigot.world.vector.Vector2D
import net.minecraft.server.v1_8_R3.AxisAlignedBB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class WorldCuboid {

    val CHUNK_SHIFTS = 4

    private var worldName = "world"

    private var minX = 0
    private var minY = 0
    private var minZ = 0

    private var maxX = 0
    private var maxY = 0
    private var maxZ = 0

    constructor(
        minX: Int,
        minY: Int,
        minZ: Int,
        maxX: Int,
        maxY: Int,
        maxZ: Int
    ) : this("world", minX, minY, minZ, maxX, maxY, maxZ)

    constructor(
        worldName: String,
        minX: Int,
        minY: Int,
        minZ: Int,
        maxX: Int,
        maxY: Int,
        maxZ: Int
    ) {
        this.worldName = worldName
        this.minX = minX.coerceAtMost(maxX)
        this.minY = minY.coerceAtMost(maxY)
        this.minZ = minZ.coerceAtMost(maxZ)
        this.maxX = minX.coerceAtLeast(maxX)
        this.maxY = minY.coerceAtLeast(maxY)
        this.maxZ = minZ.coerceAtLeast(maxZ)
    }

    constructor(min: Location, max: Location) : this(
        min.world.name,
        min.blockX,
        min.blockY,
        min.blockZ,
        max.blockX,
        max.blockY,
        max.blockZ
    )

    constructor(min: SerializedLocation, max: SerializedLocation) : this(
        min.worldName,
        min.getBlockX(),
        min.getBlockY(),
        min.getBlockZ(),
        max.getBlockX(),
        max.getBlockY(),
        max.getBlockZ()
    )

    constructor(worldName: String, min: Vector, max: Vector) : this(
        worldName,
        min.blockX,
        min.blockY,
        min.blockZ,
        max.blockX,
        max.blockY,
        max.blockZ
    )

    constructor(min: Location, max: Location, world: World) : this(
        world.name,
        min.blockX,
        min.blockY,
        min.blockZ,
        max.blockX,
        max.blockY,
        max.blockZ
    )

    fun getBukkitWorld() = Bukkit.getWorld(worldName)

    fun getMinLocation() = Location(
        this.getBukkitWorld(),
        minX.toDouble(),
        minY.toDouble(),
        minZ.toDouble()
    )

    fun getMaxLocation() = Location(
        this.getBukkitWorld(),
        maxX.toDouble(),
        maxY.toDouble(),
        maxZ.toDouble()
    )

    fun getMinVector() = Vector(minX, minY, minZ)

    fun getMaxVector() = Vector(maxX, maxY, maxZ)

    fun getLocations(callback: Consumer<Location>) {
        this.getBlocks { block: Block ->
            callback.accept(
                block.location
            )
        }
    }

    fun getBlocks(callback: Consumer<Block>) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    callback.accept(this.getBukkitWorld().getBlockAt(x, y, z))
                }
            }
        }
    }

    fun getSolidBlocks(callback: Consumer<Block>) {
        this.getBlocks { block: Block? ->
            if (block != null && block.type != Material.AIR) {
                callback.accept(block)
            }
        }
    }

    fun getWalls(callback: Consumer<Block>) {
        val world = this.getBukkitWorld()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                callback.accept(world.getBlockAt(x, y, minZ))

                if (minZ != maxZ) {
                    callback.accept(world.getBlockAt(x, y, maxZ))
                }
            }
        }

        for (z in minZ..maxZ) {
            for (y in minY..maxY) {
                callback.accept(world.getBlockAt(minX, y, z))

                if (minX != maxX) {
                    callback.accept(world.getBlockAt(maxX, y, z))
                }
            }
        }
    }

    fun getHollow(callback: Consumer<Block?>) {
        this.getWalls { block: Block? ->
            callback.accept(
                block
            )
        }

        val world = this.getBukkitWorld()

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                callback.accept(world.getBlockAt(x, minY, z))

                if (maxY != minY) {
                    callback.accept(world.getBlockAt(x, maxY, z))
                }
            }
        }
    }

    fun getRoof(callBack: Consumer<Block?>) {
        this.getBlocks { block: Block ->
            if (block.location.blockY == maxY) {
                callBack.accept(block)
            }
        }
    }

    fun getFloor(callBack: Consumer<Block>) {
        this.getBlocks { block: Block ->
            if (block.location.blockY == minY) {
                callBack.accept(block)
            }
        }
    }

    fun getBorder(callBack: Consumer<Block>) {
        this.getBlocks { block: Block ->
            if (block.location.blockY == maxY) {
                callBack.accept(block)
            }
            if (block.location.blockY == minY) {
                callBack.accept(block)
            }
        }

        this.getWalls(callBack)
    }

    fun getChunks(callback: Consumer<Vector2D>) {
        this.getChunks { x: Int, z: Int -> callback.accept(Vector2D(x, z)) }
    }

    fun getChunks(callback: BiConsumer<Int, Int>) {
        for (x in minX.shr(CHUNK_SHIFTS)..maxX.shr(CHUNK_SHIFTS)) {
            for (z in minZ.shr(CHUNK_SHIFTS)..maxZ.shr(CHUNK_SHIFTS)) {
                callback.accept(x, z)
            }
        }
    }

    fun getEntities(): List<Entity?> {
        return this.getEntities { true }
    }

    fun getEntities(predicate: Predicate<in Entity>?): List<Entity> {
        val axisAlignedBB = AxisAlignedBB(
            minY.toDouble(),
            minY.toDouble(),
            minZ.toDouble(),
            maxX.toDouble(),
            maxY.toDouble(),
            maxZ.toDouble()
        )

        val worldServer = (getBukkitWorld() as CraftWorld).handle

        return worldServer.a(
            null as net.minecraft.server.v1_8_R3.Entity?,
            axisAlignedBB
        ) {
            if (predicate === null) return@a true

            it !== null && predicate.test(it.bukkitEntity)
        }.stream().map { it.bukkitEntity }.collect(Collectors.toList())
    }

    fun destroy(removeEntities: Boolean) {
        this.getSolidBlocks { block: Block ->
            block.type = Material.AIR
        }

        if (removeEntities) {
            this.getBukkitWorld().entities.stream()
                .filter { entity: Entity ->
                    this.contains(
                        entity.location,
                        true
                    )
                }
                .filter { entity: Entity? -> entity !is Player }
                .forEach { entity: Entity -> entity.remove() }
        }
    }

    fun getCenter(): Location {
        val x = (maxX - minX) / 2.0
        val y = (maxY - minY) / 2.0
        val z = (maxZ - minZ) / 2.0

        return Location(getBukkitWorld(), minX + x, minY + y, minZ + z)
    }

    fun getWidth() = maxX - minX + 1

    fun getLength() = maxZ - minZ + 1

    fun getHeigth() = maxY - minY + 1

    fun getSize() = this.getWidth() * this.getLength() * this.getHeigth()

    fun intersects(cuboid: WorldCuboid): Boolean {
        if (minX > cuboid.maxX || cuboid.minX > maxX) return false

        if (minZ > cuboid.maxZ || cuboid.minZ > maxZ) return false

        return !(minY > cuboid.maxY || cuboid.minY > maxY)
    }

    fun containsAll(cuboid: WorldCuboid): Boolean {
        if (cuboid.maxX > maxX || cuboid.minX < minX) return false

        if (cuboid.maxY > maxY || cuboid.minY < minY) return false

        return !(cuboid.maxZ > maxZ || cuboid.minZ < minZ)
    }

    fun contains(x: Int, y: Int, z: Int) = x in minX..maxX && y in minY..maxY && z in minZ..maxZ

    fun contains(location: Location?, sameWorld: Boolean): Boolean {
        if (location == null || sameWorld && location.world.name != worldName) return false

        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        return contains(x, y, z)
    }

    fun expand(x: Int, y: Int, z: Int): WorldCuboid = WorldCuboid(worldName, minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z)

    fun contract(x: Int, y: Int, z: Int): WorldCuboid = expand(-x, -y, -z)

    fun clone(): WorldCuboid = WorldCuboid(worldName, minX, minY, minZ, maxX, maxY, maxZ)

    operator fun iterator() = object : MutableIterator<Block?> {
        private var nextX = minX
        private var nextY = minY
        private var nextZ = minZ

        override fun hasNext(): Boolean {
            return nextX != Int.MIN_VALUE
        }

        override fun next(): Block {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val block = getBukkitWorld().getBlockAt(nextX, nextY, nextZ)

            if (++nextX > maxX) {
                nextX = minX
                if (++nextY > maxY) {
                    nextY = minY
                    if (++nextZ > maxZ) {
                        nextX = Int.MIN_VALUE
                    }
                }
            }

            return block
        }

        override fun remove() {
            TODO("Not yet implemented")
        }
    }

}