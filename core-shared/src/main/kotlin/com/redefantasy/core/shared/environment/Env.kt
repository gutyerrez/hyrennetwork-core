package com.redefantasy.core.shared.environment

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.misc.utils.FileUtils
import java.io.File
import java.io.FileNotFoundException

/**
 * @author SrGutyerrez
 **/
object Env {

    private val ENVIRONMENT_MAP = mutableMapOf<String, JsonElement?>()

    private var INITIALIZED = false

    private fun initialize() {
        val file = File("/home/configuration/environment.json")

        if (!file.exists()) {
            throw FileNotFoundException("Can't find environment file")
        }

        val contents = FileUtils.readFileToString(file)

        val json = CoreConstants.GSON.fromJson(contents, JsonObject::class.java)

        FileUtils.mapJsonObject("", json, ENVIRONMENT_MAP)

        INITIALIZED = true
    }

    fun get(key: String): JsonElement? {
        if (!INITIALIZED) {
            this.initialize()
        }

        return ENVIRONMENT_MAP[key]
    }

    fun getString(key: String, defaultValue: String = ""): String {
        val jsonElement = this.get(key)

        return jsonElement?.asString ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        val jsonElement = this.get(key)

        return jsonElement?.asInt ?: defaultValue
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        val jsonElement = this.get(key)

        return jsonElement?.asDouble ?: defaultValue
    }

    fun getFloat(key: String, defaultValue: Float = 0F): Float {
        val jsonElement = this.get(key)

        return jsonElement?.asFloat ?: defaultValue
    }

    fun getLong(key: String, defaultValue: Long = 0): Long {
        val jsonElement = this.get(key)

        return jsonElement?.asLong ?: defaultValue
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val jsonElement = this.get(key)

        return jsonElement?.asBoolean ?: defaultValue
    }

    fun getShort(key: String, defaultValue: Short = 0): Short {
        val jsonElement = this.get(key)

        return jsonElement?.asShort ?: defaultValue
    }

}