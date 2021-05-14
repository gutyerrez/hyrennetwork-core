package net.hyren.core.shared.misc.utils

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream

/**
 * @author SrGutyerrez
 **/
object FileUtils {

    fun readFileToString(file: File): String {
        val fileOutputStream = FileInputStream(file)

        fileOutputStream.use {
            return IOUtils.toString(it, Charsets.UTF_8)
        }
    }

}