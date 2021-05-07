package net.hyren.core.shared.misc.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
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

    fun mapJsonObject(key: String, jsonObject: JsonObject, map: MutableMap<String, JsonElement?>) {
        jsonObject.entrySet().forEach {
            val builder = StringBuilder(key)

            if (key.length == 0) {
                builder.append(it.key)
            } else {
                builder.append(".").append(it.key)
            }

            if (it.value.isJsonObject) {
                mapJsonObject(builder.toString(), it.value.asJsonObject, map)
            } else {
                map[builder.toString()] = it.value
            }
        }
    }

}