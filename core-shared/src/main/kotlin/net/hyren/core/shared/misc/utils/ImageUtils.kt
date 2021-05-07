package net.hyren.core.shared.misc.utils

import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.net.URL
import javax.imageio.ImageIO

/**
 * @author Gutyerrez
 */
object ImageUtils {

    fun getImage(url: URL): BufferedImage {
        val connection = url.openConnection()

        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
        connection.connect()
        connection.getInputStream()

        val bufferedInputStream = BufferedInputStream(connection.getInputStream())

        return ImageIO.read(bufferedInputStream)
    }

}