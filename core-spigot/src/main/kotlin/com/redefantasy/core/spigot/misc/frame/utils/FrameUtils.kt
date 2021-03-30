package com.redefantasy.core.spigot.misc.frame.utils

import org.bukkit.Bukkit
import org.bukkit.map.MapView
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * @author Gutyerrez
 */
object FrameUtils {

    fun resize(
        image: Image,
        width: Int,
        height: Int
    ): BufferedImage {
        val image = image.getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
        )

        if (image is BufferedImage) return image

        val bufferedImage = BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_RGB
        )

        val graphics2D = bufferedImage.createGraphics()

        graphics2D.drawImage(
            image,
            0,
            0,
            null
        )
        graphics2D.dispose()

        return bufferedImage
    }

    fun getMapId(mapView: MapView?) = mapView?.id

    fun getDefaultWorld() = Bukkit.getWorld("world")

    fun getPanes(size: Int): Int {
        var size = size

        while (size % 128 != 0) {
            size++
        }

        return size / 128
    }

}