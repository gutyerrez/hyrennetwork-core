package com.redefantasy.core.spigot.misc.frame.render

import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapCursorCollection
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.image.BufferedImage

/**
 * @author Gutyerrez
 */
class FrameRenderer(
    val bufferedImage: BufferedImage,
    var imageRendered: Boolean = false
) : MapRenderer() {

    override fun render(
        mapView: MapView,
        mapCanvas: MapCanvas,
        player: Player
    ) {
        mapCanvas.cursors = MapCursorCollection()

        if (this.imageRendered) return

        mapCanvas.drawImage(0, 0, this.bufferedImage)

        this.imageRendered = true
    }

}