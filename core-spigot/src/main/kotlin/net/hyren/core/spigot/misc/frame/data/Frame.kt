package net.hyren.core.spigot.misc.frame.data

import net.hyren.core.shared.misc.utils.ImageUtils
import net.hyren.core.spigot.CoreSpigotPlugin
import net.hyren.core.spigot.misc.frame.FrameManager
import net.hyren.core.spigot.misc.frame.format.FrameImageFormat
import net.hyren.core.spigot.misc.frame.render.FrameRenderer
import net.hyren.core.spigot.misc.frame.utils.FrameUtils
import net.hyren.core.spigot.misc.utils.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
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

    private lateinit var bufferedImage: BufferedImage

    private var lengthX: Int = 0
    private var lengthY: Int = 0

    private lateinit var frameImageFormat: FrameImageFormat
    private lateinit var mapCollection: MutableMap<FrameRelativeLocation, MapView>

    private var location: Location? = null
    private var blockFace: BlockFace? = null

    private lateinit var mapFrames: MutableList<ItemFrame>

    var interactConsumer: Consumer<Player>? = null

    init {
        val bufferedImage = ImageUtils.getImage(url)
        val extension = url.file.substring(url.file.length - 3)
        val frameImageFormat = FrameImageFormat.fromExtension(extension)

        if (frameImageFormat === null) throw IllegalArgumentException(
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

    fun isPlaced() = this.location !== null && this.blockFace !== null

    fun place(
        location: Location,
        blockFace: BlockFace
    ) {
        this.mapFrames = mutableListOf()

        val world = location.world

        val x = location.x
        val y = location.y
        val z = location.z

        val mapsView = mutableMapOf<Location, MapView>()

        mapCollection.entries.forEachIndexed { index, entry ->
            val frameRelativeLocation = entry.key
            val mapView = entry.value

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

            mapsView[location] = mapView
        }

        this.location = location
        this.blockFace = blockFace

        mapsView.forEach { (location, mapView) ->
            mapView.world = this.location!!.world

            Bukkit.getScheduler().runTaskLater(
                CoreSpigotPlugin.instance,
                {
                    if (!location.chunk.isLoaded) location.chunk.load()

                    val craftWorld = world as CraftWorld

                    val itemFrame = craftWorld.spawn(
                        location,
                        ItemFrame::class.java,
                        blockFace
                    )

                    itemFrame.setFacingDirection(blockFace, true)

                    itemFrame.item = ItemBuilder(Material.MAP)
                        .durability(mapView.id.toInt())
                        .lore(
                            arrayOf(
                                UUID.randomUUID().toString()
                            )
                        ).build()

                    mapFrames.add(itemFrame)

                    if (this.interactConsumer != null) {
                        FrameManager.INTERACTABLE_FRAMES[
                                itemFrame.uniqueId
                        ] = this
                    }
                },
                20L
            )
        }

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