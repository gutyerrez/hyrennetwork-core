package com.redefantasy.core.shared

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.GsonBuilder
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.misc.cooldowns.CooldownManager
import com.redefantasy.core.shared.misc.jackson.ServerDeserializer
import com.redefantasy.core.shared.misc.jackson.ServerSerializer
import com.redefantasy.core.shared.servers.data.Server
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

    val GSON = GsonBuilder()
        .setPrettyPrinting()
        .create()

    val OK_HTTP = OkHttpClient()
    val RANDOM = Random()
    val DATE_TIME_ZONE = DateTimeZone.forID("America/Sao_Paulo")

    val COOLDOWNS = CooldownManager()

    val WHITELISTED_USERS = listOf(
        "Gutyerrez",
        "ImRamon",
        "CONSOLE",
        "VICTORBBBBR"
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
        JACKSON.setDefaultPrettyPrinter(DefaultPrettyPrinter())

        val module = SimpleModule()

        // Server serializer
        module.addSerializer(
            Server::class.java,
            ServerSerializer()
        )
        module.addDeserializer(
            Server::class.java,
            ServerDeserializer()
        )

        JACKSON.registerModule(module)
    }


    fun fetchLobbyApplication(): Application? {
        val application = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
            ApplicationType.LOBBY
        ).shuffled()
            .stream()
            .filter {
                CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
                    it,
                    ApplicationStatus::class
                ) !== null
            }
            .filter {
                val usersByApplication = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(it)

                usersByApplication.size < it.slots ?: 0
            }
            .min { application1, application2 ->
                val usersByApplication1 = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(application1)
                val usersByApplication2 = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByApplication(application2)

                usersByApplication1.size.compareTo(usersByApplication2.size)
            }
            .orElse(null)

        return application
    }

}