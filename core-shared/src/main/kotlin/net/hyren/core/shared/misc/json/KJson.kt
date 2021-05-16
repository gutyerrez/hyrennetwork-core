package net.hyren.core.shared.misc.json

import com.google.common.base.Enums
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
                        }.asJsonObject())
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
                            put("server_type", Optional.ofNullable(
                                value.serverType
                            ).map { it.name }.orElse(null))
                        }.asJsonObject())
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
                            jsonObject.getValue("server_type").asEnum(ServerType::class)!!
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
                                    put("server_type", Optional.ofNullable(
                                        it.serverType
                                    ).map { it.name }.orElse(null))
                                }
                            }?.let { put("server", it) } ?: put("server", "undefined")

                            put("inet_socket_address", buildJsonObject {
                                put("host", value.inetSocketAddress.address.hostAddress)
                                put("port", value.inetSocketAddress.port)
                            })

                            put("online_since", value.onlineSince)
                            put("heap_size", value.heapSize ?: 0)
                            put("heap_max_size", value.heapMaxSize ?: 0)
                            put("heap_free_size", value.heapFreeSize ?: 0)
                            put("online_players", value.onlinePlayers)
                        }.asJsonObject())
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): ApplicationStatus {
                        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

                        return ApplicationStatus(
                            jsonObject.getValue("application_name").asString(),
                            jsonObject.getValue("application_type").asEnum(ApplicationType::class)!!,
                            if (jsonObject.getValue("server") is JsonPrimitive) {
                                null
                            } else Server(
                                EntityID(
                                    jsonObject.getValue("server").asJsonObject().getValue("name").asString(),
                                    ServersTable
                                ),
                                jsonObject.getValue("server").asJsonObject().getValue("display_name").asString(),
                                jsonObject.getValue("server").asJsonObject().getValue("server_type").asEnum(ServerType::class)!!
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
                Array<PunishCategory>::class,
                object : KSerializer<Array<PunishCategory>>() {
                    override fun serialize(
                        jsonEncoder: JsonEncoder,
                        value: Array<PunishCategory>
                    ) {
                        jsonEncoder.encodeJsonElement(buildJsonArray {
                            value.forEach {
                                addJsonObject {
                                    put("name", it.name.value)
                                    put("display_name", it.displayName)
                                    put("description", it._description)
                                    put("punish_durations", buildJsonArray {
                                        it.punishDurations.forEach {
                                            add(buildJsonObject {
                                                put("duration", it.duration)
                                                put("punish_type", Optional.ofNullable(
                                                    it.punishType
                                                ).map { it.name }.orElse(null))
                                            })
                                        }
                                    })
                                    put("group", Optional.ofNullable(
                                        it.group
                                    ).map { it.name }.orElse(null))
                                    put("enabled", it.enabled)
                                }
                            }
                        }.asJsonArray())
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): Array<PunishCategory> {
                        val jsonArray = jsonDecoder.decodeJsonElement().asJsonArray()

                        val array = sizedArray<PunishCategory>(jsonArray.size)

                        jsonArray.forEachIndexed { index, it ->
                            val it = it.asJsonObject()

                            array[index] = PunishCategory(
                                EntityID(
                                    it.getValue("name").asString(),
                                    PunishCategoriesTable
                                ),
                                it.getValue("display_name").asString(),
                                it.getValue("description").asString(),
                                sizedArray<PunishDuration>(it.getValue("punish_durations").asJsonArray().size).apply {
                                    it.getValue("punish_durations").asJsonArray().forEachIndexed { index, jsonElement ->
                                        val _jsonObject = jsonElement.asJsonObject()

                                        this[index] = PunishDuration(
                                            _jsonObject.getValue("duration").asLong(),
                                            _jsonObject.getValue("punish_type").asEnum(PunishType::class)!!
                                        )
                                    }
                                },
                                it.getValue("group").asEnum(Group::class)!!,
                                it.getValue("enabled").asBoolean()
                            )
                        }

                        return array
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
                            put("preference_state", Optional.ofNullable(
                                value.preferenceState
                            ).map { it.name }.orElse(null))
                        }.asJsonObject())
                    }

                    override fun deserialize(
                        jsonDecoder: JsonDecoder
                    ): Preference {
                        val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                        return Preference(
                            jsonObject.getValue("name").asString(),
                            jsonObject.getValue("preference_state").asEnum(
                                PreferenceState::class
                            )!!
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
                        }.asJsonObject())
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
                                put("server_type", Optional.ofNullable(
                                    value.server.serverType
                                ).map { it.name }.orElse(null))
                            })
                        }.asJsonObject())
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
                                jsonObject.getValue("server").asJsonObject().getValue("server_type").asEnum(ServerType::class)!!
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

    final override fun serialize(
        encoder: Encoder,
        value: T
    ) = serialize(
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

fun JsonElement.asString(): String = this.toString()

fun JsonElement.asInt(): Int = this.jsonPrimitive.int

fun JsonElement.asDouble(): Double = this.jsonPrimitive.double

fun JsonElement.asFloat(): Float = this.jsonPrimitive.float

fun JsonElement.asLong(): Long = this.jsonPrimitive.long

fun JsonElement.asBoolean(): Boolean = this.jsonPrimitive.boolean

fun <T: Enum<T>> JsonElement.asEnum(
    kClass: KClass<T>
): T? = Enums.getIfPresent(
    kClass.java,
    this.asString().replace("\"", "")
).orNull()

fun JsonElement.asJsonObject(): JsonObject = this.jsonObject

fun JsonElement.asJsonArray(): JsonArray = this.jsonArray