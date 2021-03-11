package com.redefantasy.core.shared

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.joda.time.DateTimeZone
import java.util.*

/**
 * @author SrGutyerrez
 **/
object CoreConstants {

    const val HOME_FOLDER = "/home"
    const val MAX_LOGIN_ATTEMPTS = 3

    val CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

    val JACKSON = ObjectMapper()
    val GSON = Gson()
    val OK_HTTP = OkHttpClient()
    val RANDOM = Random()
    val DATE_TIME_ZONE = DateTimeZone.forID("America/Sao_Paulo")

    val WHITELISTED_USERS = listOf(
        "Gutyerrez",
        "ImRamon"
    )

    val UN_LOGGED_ALLOWED_COMMANDS = listOf(
        "/logar",
        "/registrar",
        "/login",
        "/register"
    )

    init {
        JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        JACKSON.configure(DeserializationFeature.WRAP_EXCEPTIONS, true)
        JACKSON.registerModule(GuavaModule())
        JACKSON.registerModule(KotlinModule())
        JACKSON.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        JACKSON.setVisibility(
                JACKSON.serializationConfig.defaultVisibilityChecker
                        .with(JsonAutoDetect.Visibility.NONE)
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        )
    }

}