package com.redefantasy.core.spigot.misc.frame.data

import com.redefantasy.core.shared.misc.utils.ImageUtils
import com.redefantasy.core.spigot.CoreSpigotPlugin
import com.redefantasy.core.spigot.misc.frame.FrameManager
import com.redefantasy.core.spigot.misc.frame.format.FrameImageFormat
import com.redefantasy.core.spigot.misc.frame.render.FrameRenderer
import com.redefantasy.core.spigot.misc.frame.utils.FrameUtils
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.map.MapView
import java.awt.image.BufferedImage
import java.net.URL
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
data class Frame(val url: URL) {

    private lateinit var id: UUID
    private lateinit var bufferedImage: BufferedImage

    private var lengthX: Int = 0
    private var lengthY: Int = 0

    private lateinit var frameImageFormat: FrameImageFormat
    private lateinit var mapCollection: MutableMap<FrameRelativeLocation, MapView>

    private var location: Location? = null
    private var blockFace: BlockFace? = null

    private lateinit var mapFrames: MutableList<ItemFrame>

    private var interactConsumer: Consumer<Player>? = null

    init {
        val bufferedImage = ImageUtils.getImage(url)
        val extension = url.file.substring(url.file.length - 3)
        val frameImageFormat = FrameImageFormat.fromExtension(extension)

        if (frameImageFormat === null) throw java.lang.IllegalArgumentException(
            "Invalid file extension. Supported extensions: ${
                Arrays.stream(FrameImageFormat.values()).map { it.extension }.collect(Collectors.joining(", "))
            }."
        )

        this.init(bufferedImage, frameImageFormat)
    }

    fun init(
        bufferedImage: BufferedImage,
        frameImageFormat: FrameImageFormat
    ) {
        val xPanes = FrameUtils.getPanes(bufferedImage.width)
        val yPanes = FrameUtils.getPanes(bufferedImage.height)

        this.id = UUID.nameUUIDFromBytes(("Frame:${RandomStringUtils.random(16)}").toByteArray(Charsets.UTF_8))
        this.bufferedImage = FrameUtils.resize(
            bufferedImage,
            xPanes * 128,
            yPanes * 128
        )
        this.lengthX = xPanes
        this.lengthY = yPanes
        this.frameImageFormat = frameImageFormat
        this.mapCollection = mutableMapOf()

        this.loadFrame()
    }

    fun loadFrame() {
        for (x in 0 until lengthX) {
            for (y in 0 until lengthY) {
                this.initFrame(
                    x,
                    y,
                    Bukkit.createMap(
                        FrameUtils.getDefaultWorld()
                    )
                )
            }
        }
    }

    fun initFrame(
        x: Int,
        y: Int,
        mapView: MapView
    ) {
        val bufferedImage = this.bufferedImage.getSubimage(x * 128, y * 128, 128, 128)

        mapView.renderers.forEach { mapView.removeRenderer(it) }

        mapView.addRenderer(
            FrameRenderer(
                bufferedImage
            )
        )

        mapCollection[
                FrameRelativeLocation(x, y)
        ] = mapView
    }

    fun getItem() = this.getItems().stream().findAny().orElse(null)

    fun getItems(): LinkedHashSet<ItemStack> {
        val items = linkedSetOf<ItemStack>()

        for (y in 0 until lengthY) {
            for (x in 0 until lengthX) {
                for (frameRelativeLocation in mapCollection.keys) {
                    if (frameRelativeLocation.x != x || frameRelativeLocation.y != y) continue

                    items.add(
                        ItemBuilder(
                            Material.MAP
                        ).durability(
                            FrameUtils.getMapId(
                                mapCollection[frameRelativeLocation]
                            )?.toInt() ?: 0
                        ).name(
                            "X: ${x + 1} Y: ${y + 1}"
                        ).lore(
                            arrayOf(UUID.randomUUID().toString())
                        ).build()
                    )
                }
            }
        }

        return items
    }

    fun addInteractListener(consumer: Consumer<Player>) {
        this.interactConsumer = consumer

        if (this.isPlaced()) {
            FrameManager.INTERACTABLE_FRAMES[this.id] = this
        }
    }

    fun isPlaced() = this.location !== null && this.blockFace !== null

    fun place(
        location: Location,
        blockFace: BlockFace
    ) {
        this.mapFrames = mutableListOf()

        val world = location.world

        val x = location.x; val y = location.y; val z = location.z

        val mapsView = mutableMapOf<Location, MapView>()

        mapCollection.forEach { frameRelativeLocation, mapView ->
            mapView.world = world

            val location = when (blockFace) {
                BlockFace.SOUTH -> Location(
                    world,
                    x + frameRelativeLocation.x,
                    y - frameRelativeLocation.y,
                    z + 1
                )
                BlockFace.NORTH -> Location(
                    world,
                    x - frameRelativeLocation.x,
                    y - frameRelativeLocation.y,
                    z - 1
                )
                BlockFace.WEST -> Location(
                    world,
                    x - 1,
                    y - frameRelativeLocation.y,
                    z + frameRelativeLocation.x
                )
                BlockFace.EAST -> Location(
                    world,
                    x + 1,
                    y - frameRelativeLocation.y,
                    z - frameRelativeLocation.x
                )
                else -> throw IllegalArgumentException("BlockFace argument error. Use NORTH, SOUTH, EAST or WEST.")
            }

//            if (location.block.type != Material.AIR && location.block.type != Material.ITEM_FRAME) {
//                throw IllegalArgumentException("The location is not empty. Location: $location")
//            }

            mapsView[location] = mapView
        }

        this.location = location
        this.blockFace = blockFace

        mapsView.forEach { location, mapView ->
            mapView.world = this.location!!.world

            val clonedLocation = location.clone()

            clonedLocation.world.entities.stream()
                .filter { it.type == EntityType.ITEM_FRAME }
                .forEach {
                    if (it.location.blockX == clonedLocation.blockX && it.location.blockY == clonedLocation.blockY && it.location.blockZ == clonedLocation.blockZ) {
                        it.remove()
                    }
                }

            Bukkit.getScheduler().runTaskLater(
                CoreSpigotPlugin.instance,
                {
                    if (!location.chunk.isLoaded) location.chunk.load()

                    val craftWorld = world as CraftWorld

                    val itemFrame = craftWorld.createEntity(
                        location,
                        ItemFrame::class.java,
                        blockFace
                    ).bukkitEntity as ItemFrame

                    itemFrame.item = ItemBuilder(Material.MAP)
                        .durability(mapView.id.toInt())
                        .lore(
                            arrayOf(
                                UUID.randomUUID().toString()
                            )
                        ).build()

                    mapFrames.add(itemFrame)
                },
                20L
            )
        }

        if (this.interactConsumer !== null) FrameManager.INTERACTABLE_FRAMES[this.id] = this
    }


    data class FrameRelativeLocation(
        val x: Int,
        val y: Int
    ) {

        companion object {

            fun fromString(stringified: String): FrameRelativeLocation? {
                val args = stringified.split(":")

                if (args.size != 2) return null

                return FrameRelativeLocation(
                    args[0].toInt(),
                    args[1].toInt()
                )
            }

        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            if (javaClass != other?.javaClass) return false

            other as FrameRelativeLocation

            if (x != other.x) return false

            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            return this.x + this.y
        }

    }

}