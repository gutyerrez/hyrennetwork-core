package com.redefantasy.core.spigot.misc.frame.format

/**
 * @author Gutyerrez
 */
enum class FrameImageFormat(
    val extension: String
) {

    PNG("png"),
    JPEG("jpg");

    companion object {

        fun fromExtension(extension: String) = values().find { it.extension == extension }

    }

}