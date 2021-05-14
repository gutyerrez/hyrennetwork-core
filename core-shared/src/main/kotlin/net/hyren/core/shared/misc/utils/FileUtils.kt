package net.hyren.core.shared.misc.utils

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
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

    fun mapJsonObject(key: String = "", jsonObject: JsonObject, map: MutableMap<String, JsonElement?>) {
        jsonObject.entries.forEach {
            val builder = StringBuilder(key)

            if (key.isEmpty()) {
                builder.append(it.key)
            } else {
                builder.append(".").append(it.key)
            }

            if (it.value is JsonObject) {
                mapJsonObject(builder.toString(), it.value.jsonObject, map)
            } else {
                map[builder.toString()] = it.value
            }
        }
    }

}