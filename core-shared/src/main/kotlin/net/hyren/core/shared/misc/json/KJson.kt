package net.hyren.core.shared.misc.json

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.plus
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.core.shared.misc.preferences.PreferenceState
import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.misc.punish.PunishType
import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.misc.punish.category.storage.table.PunishCategoriesTable
import net.hyren.core.shared.misc.punish.durations.PunishDuration
import net.hyren.core.shared.misc.report.category.data.ReportCategory
import net.hyren.core.shared.misc.report.category.storage.table.ReportCategoriesTable
import net.hyren.core.shared.servers.ServerType
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.table.ServersTable
import net.hyren.core.shared.users.reports.data.Report
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.net.InetSocketAddress
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
object KJson {

    val _json: Json = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            // InetSocketAddress serializer
            contextual(
                InetSocketAddress::class,
                object : KSerializer<InetSocketAddress>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: InetSocketAddress
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("host", value.address.hostAddress)
                            put("port", value.port)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): InetSocketAddress {
                        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

                        return InetSocketAddress(
                            jsonObject.getValue("host").asString(),
                            jsonObject.getValue("port").asInt()
                        )
                    }
                }
            )

            // Server serializer
            contextual(
                Server::class,
                object : KSerializer<Server>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: Server
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("name", value.name.value)
                            put("display_name", value.displayName)
                            put("server_type", value.serverType.name)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): Server {
                        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

                        return Server(
                            EntityID(
                                jsonObject.getValue("name").asString(),
                                ServersTable
                            ),
                            jsonObject.getValue("display_name").asString(),
                            ServerType.valueOf(
                                jsonObject.getValue("server_type").asString()
                            )
                        )
                    }
                }
            )

            // ApplicationStatus serializer
            contextual(
                ApplicationStatus::class,
                object : KSerializer<ApplicationStatus>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: ApplicationStatus
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("application_name", value.applicationName)
                            put("application_type", value.applicationType.name)
                            value.server?.let {
                                return@let buildJsonObject {
                                    put("name", it.name.value)
                                    put("display_name", it.displayName)
                                    put("server_type", it.serverType.name)
                                }
                            }?.let { put("server", it) } ?: put("server", "undefined")

                            put("inet_socket_address", buildJsonObject {
                                put("host", value.inetSocketAddress.address.hostAddress)
                                put("port", value.inetSocketAddress.port)
                            })

                            put("online_since", value.onlineSince)
                            put("heap_size", value.heapSize)
                            put("heap_max_size", value.heapMaxSize)
                            put("heap_free_size", value.heapFreeSize)
                            put("online_players", value.onlinePlayers)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): ApplicationStatus {
                        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

                        return ApplicationStatus(
                            jsonObject.getValue("application_name").asString(),
                            ApplicationType.valueOf(
                                jsonObject.getValue("application_type").asString()
                            ),
                            if (jsonObject.getValue("server") is JsonPrimitive) {
                                null
                            } else Server(
                                EntityID(
                                    jsonObject.getValue("server").asJsonObject().getValue("name").asString(),
                                    ServersTable
                                ),
                                jsonObject.getValue("server").asJsonObject().getValue("display_name").asString(),
                                ServerType.valueOf(
                                    jsonObject.getValue("server").asJsonObject().getValue("server_type").asString()
                                )
                            ),
                            InetSocketAddress(
                                jsonObject.getValue("inet_socket_address").asJsonObject().getValue("host").asString(),
                                jsonObject.getValue("inet_socket_address").asJsonObject().getValue("port").asInt()
                            ),
                            jsonObject.getValue("online_since").asLong()
                        ).apply {
                            heapSize = jsonObject.getValue("heap_size").asLong()
                            heapMaxSize = jsonObject.getValue("heap_max_size").asLong()
                            heapFreeSize = jsonObject.getValue("heap_free_size").asLong()
                            onlinePlayers = jsonObject.getValue("online_players").asInt()
                        }
                    }
                }
            )

            // PunishCategory serializer
            contextual(
                PunishCategory::class,
                object : KSerializer<PunishCategory>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: PunishCategory
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("name", value.name.value)
                            put("display_name", value.displayName)
                            put("description", value._description)
                            put("punish_durations", buildJsonArray {
                                value.punishDurations.forEach {
                                    add(buildJsonObject {
                                        put("duration", it.duration)
                                        put("punish_type", it.punishType.name)
                                    })
                                }
                            })
                            put("group", value.group.name)
                            put("enabled", value.enabled)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): PunishCategory {
                        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

                        return PunishCategory(
                            EntityID(
                                jsonObject.getValue("name").asString(),
                                PunishCategoriesTable
                            ),
                            jsonObject.getValue("display_name").asString(),
                            jsonObject.getValue("description").asString(),
                            sizedArray<PunishDuration>(jsonObject.getValue("punish_durations").asJsonArray().size).apply {
                                jsonObject.getValue("punish_durations").asJsonArray().forEachIndexed { index, jsonElement ->
                                    val _jsonObject = jsonElement.asJsonObject()

                                    this[index] = PunishDuration(
                                        _jsonObject.getValue("duration").asLong(),
                                        PunishType.valueOf(
                                            _jsonObject.getValue("punish_type").asString()
                                        )
                                    )
                                }
                            },
                            Group.valueOf(jsonObject.getValue("group").asString()),
                            jsonObject.getValue("enabled").asBoolean()
                        )
                    }
                }
            )

            // Preference serializer
            contextual(
                Preference::class,
                object : KSerializer<Preference>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: Preference
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("name", value.name)
                            put("preference_state", value.preferenceState.name)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): Preference {
                        val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                        return Preference(
                            jsonObject.getValue("name").asString(),
                            PreferenceState.valueOf(
                                jsonObject.getValue("preference_state").asString()
                            )
                        )
                    }
                }
            )

            // ReportCategory
            contextual(
                ReportCategory::class,
                object : KSerializer<ReportCategory>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: ReportCategory
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("name", value.name.value)
                            put("display_name", value.displayName)
                            put("description", value._description)
                            put("enabled", value.enabled)
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): ReportCategory {
                        val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                        return ReportCategory(
                            EntityID(
                                jsonObject.getValue("name").asString(),
                                ReportCategoriesTable
                            ),
                            jsonObject.getValue("display_name").asString(),
                            jsonObject.getValue("description").asString(),
                            jsonObject.getValue("enabled").asBoolean()
                        )
                    }
                }
            )

            // Report serializer
            contextual(
                Report::class,
                object : KSerializer<Report>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: Report
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonObject {
                            put("report_id", value.reporterId.value.toString())
                            put("report_category", buildJsonObject {
                                put("name", value.reportCategory.name.value)
                                put("display_name", value.reportCategory.displayName)
                                put("description", value.reportCategory._description)
                                put("enabled", value.reportCategory.enabled)
                            })
                            put("reported_at", value.reportedAt.toString())
                            put("server", buildJsonObject {
                                put("name", value.server.name.value)
                                put("display_name", value.server.displayName)
                                put("server_type", value.server.serverType.name)
                            })
                        })
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): Report {
                        val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                        return Report(
                            EntityID(
                                UUID.fromString(
                                    jsonObject.getValue("reporter_id").asString()
                                ),
                                UsersTable
                            ),
                            ReportCategory(
                                EntityID(
                                    jsonObject.getValue("report_category").asJsonObject().getValue("name").asString(),
                                    ReportCategoriesTable
                                ),
                                jsonObject.getValue("report_category").asJsonObject().getValue("display_name").asString(),
                                jsonObject.getValue("report_category").asJsonObject().getValue("description").asString(),
                                jsonObject.getValue("report_category").asJsonObject().getValue("enabled").asBoolean()
                            ),
                            DateTime.parse(jsonObject.getValue("reported_at").toString()),
                            Server(
                                EntityID(
                                    jsonObject.getValue("server").asJsonObject().getValue("name").asString(),
                                    ServersTable
                                ),
                                jsonObject.getValue("server").asJsonObject().getValue("display_name").asString(),
                                ServerType.valueOf(
                                    jsonObject.getValue("server").asJsonObject().getValue("server_type").asString()
                                )
                            )
                        )
                    }
                }
            )
        }
    }

    inline fun <reified T> encodeToString(
        t: T
    ) = _json.encodeToString(t)

    inline fun <reified T> encodeToJsonElement(
        t: T
    ) = _json.encodeToJsonElement(t)

    inline fun <reified T> decodeFromString(
        string: String
    ) = _json.decodeFromString<T>(string)

    inline fun <reified T> decodeFromJsonElement(
        jsonElement: JsonElement
    ) = _json.decodeFromJsonElement<T>(jsonElement)

    inline fun <reified T> registerSerializer(
        serializers: SerializersModuleBuilder.() -> Unit
    ) = _json.serializersModule.plus(SerializersModule { serializers() })

    /**
     * Class deserializers
     */

    @ExperimentalSerializationApi
    fun decodeFromString(
        kClass: KClass<*>,
        string: String
    ) = _json.serializersModule.getContextual(kClass)?.let { _json.decodeFromString(it, string) }


}

abstract class KSerializer<T> : kotlinx.serialization.KSerializer<T> {

    @ExperimentalSerializationApi
    final override val descriptor: SerialDescriptor = ContextualSerializer(
        Any::class,null, emptyArray()
    ).descriptor

    abstract fun serialize(
        jsonEncoder: JsonEncoder,
        value: T
    )

    final override fun serialize(encoder: Encoder, value: T) = serialize(
        encoder as JsonEncoder,
        value
    )

    abstract fun deserialize(
        jsonDecoder: JsonDecoder
    ): T

    final override fun deserialize(decoder: Decoder): T = deserialize(
        decoder as JsonDecoder
    )

}

public fun JsonElement.asString(): String = this.toString()

public fun JsonElement.asInt(): Int = this.jsonPrimitive.int

public fun JsonElement.asDouble(): Double = this.jsonPrimitive.double

public fun JsonElement.asFloat(): Float = this.jsonPrimitive.float

public fun JsonElement.asLong(): Long = this.jsonPrimitive.long

public fun JsonElement.asBoolean(): Boolean = this.jsonPrimitive.boolean

public fun JsonElement.asJsonObject(): JsonObject = this.jsonObject

public fun JsonElement.asJsonArray(): JsonArray = this.jsonArray