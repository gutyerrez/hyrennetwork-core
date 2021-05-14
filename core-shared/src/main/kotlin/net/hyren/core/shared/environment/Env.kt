package net.hyren.core.shared.environment

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import net.hyren.core.shared.misc.utils.FileUtils
import java.io.File
import java.io.FileNotFoundException

/**
 * @author SrGutyerrez
 **/
object Env {

    private lateinit var ENVIRONMENT_MAP: Map<String, JsonElement?>

    private var INITIALIZED = false

    private fun initialize() {
        val file = File("/home/configuration/environment.json")

        if (!file.exists()) {
            throw FileNotFoundException("Can't find environment file")
        }
        val contents = FileUtils.readFileToString(file)

        this.ENVIRONMENT_MAP = Json.decodeFromString(contents)

        println(this.ENVIRONMENT_MAP)

        INITIALIZED = true
    }

    fun get(key: String): JsonElement? {
        if (!INITIALIZED) {
            initialize()
        }

        return ENVIRONMENT_MAP[key]
    }

    fun getString(key: String, defaultValue: String = ""): String {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getFloat(key: String, defaultValue: Float = 0F): Float {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getLong(key: String, defaultValue: Long = 0): Long {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }

    fun getShort(key: String, defaultValue: Short = 0): Short {
        val jsonElement = get(key)

        return jsonElement?.let {
            Json.decodeFromJsonElement(it)
        } ?: defaultValue
    }
}