package net.hyren.core.shared.misc.json

import com.google.common.base.Enums
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.internal.*
import kotlinx.serialization.modules.*
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
import org.jetbrains.exposed.dao.id.*
import org.joda.time.DateTime
import java.net.InetSocketAddress
import java.sql.SQLException
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
object KJson {

    val _serializersModule = SerializersModule {
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

        // UUID serializer
        contextual(
            UUID::class,
            object : KSerializer<UUID>() {
                override fun serialize(
                    jsonEncoder: JsonEncoder,
                    value: UUID
                ) = jsonEncoder.encodeString(value.toString())

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ) = UUID.fromString(jsonDecoder.decodeString())
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
                                    put("punish_type", Optional.ofNullable(
                                        it.punishType
                                    ).map { it.name }.orElse(null))
                                })
                            }
                        })
                        put("group", Optional.ofNullable(
                            value.group
                        ).map { it.name }.orElse(null))
                        put("enabled", value.enabled)
                    }.asJsonObject())
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
                                    _jsonObject.getValue("punish_type").asEnum(PunishType::class)!!
                                )
                            }
                        },
                        jsonObject.getValue("group").asEnum(Group::class)!!,
                        jsonObject.getValue("enabled").asBoolean()
                    )
                }
            }
        )

        // PunishDuration serializer
        contextual(
            Array<PunishDuration>::class,
            object : KSerializer<Array<PunishDuration>>() {
                override fun serialize(
                    jsonEncoder: JsonEncoder,
                    value: Array<PunishDuration>
                ) {
                    jsonEncoder.encodeJsonElement(buildJsonArray {
                        value.forEach {
                            addJsonObject {
                                put("duration", it.duration)
                                put("punish_type", Optional.ofNullable(
                                    it.punishType
                                ).map { it.name }.orElse(null)
                                )
                            }
                        }
                    }.asJsonArray())
                }

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ): Array<PunishDuration> {
                    val jsonArray = jsonDecoder.decodeJsonElement().asJsonArray()

                    val array = sizedArray<PunishDuration>(jsonArray.size)

                    jsonArray.forEachIndexed { index, it ->
                        val it = it.asJsonObject()

                        array[index] = PunishDuration(
                            it.getValue("duration").asLong(),
                            it.getValue("punish_type").asEnum(PunishType::class)!!
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
                    })
                }

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ): Preference {
                    val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                    return Preference(
                        jsonObject.getValue("name").asString(),
                        jsonObject.getValue("preference_state").asEnum(PreferenceState::class)!!
                    )
                }
            }
        )

        // Preferences serializer
        contextual(
            Array<Preference>::class,
            object : KSerializer<Array<Preference>>() {
                override fun serialize(
                    jsonEncoder: JsonEncoder,
                    value: Array<Preference>
                ) {
                    jsonEncoder.encodeJsonElement(buildJsonArray {
                        value.forEach {
                            add(buildJsonObject {
                                put("name", it.name)
                                put("preference_state", Optional.ofNullable(
                                    it.preferenceState
                                ).map { it.name }.orElse(null))
                            })
                        }
                    }.asJsonArray())
                }

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ): Array<Preference> {
                    val jsonArray = jsonDecoder.decodeJsonElement().asJsonArray()

                    val array = sizedArray<Preference>(jsonArray.size)

                    jsonArray.forEachIndexed { index, it ->
                        val it = it.asJsonObject()

                        array[index] = Preference(
                            it.getValue("name").asString(),
                            it.getValue("preference_state").asEnum(
                                PreferenceState::class
                            )!!
                        )
                    }

                    return array
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

        // EntityID serializer
        contextual(
            EntityID::class,
            object : KSerializer<EntityID<*>>() {
                override fun serialize(
                    jsonEncoder: JsonEncoder,
                    value: EntityID<*>
                ) {
                    jsonEncoder.encodeJsonElement(buildJsonObject {
                        put("table_class_qualified_name", value.table::class.qualifiedName)

                        val value = value.value

                        when (value) {
                            is UUID -> put("id", value.toString())
                            is Int -> put("id", value)
                            is Double -> put("id", value)
                            is Float  -> put("id", value)
                            is Long -> put("id", value)
                            is String -> put("id", value)
                            else -> error("Unsupported id type ${value::class.qualifiedName}")
                        }
                    })
                }

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ): EntityID<*> {
                    val jsonObject = jsonDecoder.decodeJsonElement().asJsonObject()

                    val tableClassQualifiedName = jsonObject.getValue("table_class_qualified_name").asString()
                    val table = Class.forName(tableClassQualifiedName).kotlin.objectInstance ?: throw SQLException("Cannot find table $tableClassQualifiedName")

                    return when {
                        jsonObject.getValue("id").isInt() -> {
                            EntityID(
                                jsonObject.getValue("id").asInt(),
                                table as IdTable<Int>
                            )
                        }
                        jsonObject.getValue("id").isDouble() -> {
                            EntityID(
                                jsonObject.getValue("id").asDouble(),
                                table as IdTable<Double>
                            )
                        }
                        jsonObject.getValue("id").isFloat() -> {
                            EntityID(
                                jsonObject.getValue("id").asFloat(),
                                table as IdTable<Float>
                            )
                        }
                        jsonObject.getValue("id").isLong() -> {
                            EntityID(
                                jsonObject.getValue("id").asLong(),
                                table as IdTable<Long>
                            )
                        }
                        else -> {
                            EntityID(
                                jsonObject.getValue("id").asString(),
                                table as IdTable<String>
                            )
                        }
                    }
                }
            }
        )

        // DateTime serializer
        contextual(
            DateTime::class,
            object : KSerializer<DateTime>() {
                override fun serialize(
                    jsonEncoder: JsonEncoder,
                    value: DateTime
                ) {
                    jsonEncoder.encodeString(value.asString())
                }

                override fun deserialize(
                    jsonDecoder: JsonDecoder
                ) = DateTime.parse(jsonDecoder.decodeString())
            }
        )
    }

    val _json: Json = Json {
        isLenient = true
        serializersModule = _serializersModule
    }

    inline fun <reified T> encodeToString(
        t: T?
    ) = _json.encodeToString(t)

    inline fun <reified T> encodeToJsonElement(
        t: T?
    ) = _json.encodeToJsonElement(t)

    inline fun <reified T> decodeFromString(
        string: String?
    ) = string?.let { _json.decodeFromString<T>(it) } ?: throw NullPointerException()

    inline fun <reified T> decodeFromJsonElement(
        jsonElement: JsonElement?
    ) = jsonElement?.let { _json.decodeFromJsonElement<T>(it) } ?: throw NullPointerException()

    /**
     * Serializers register
     */

    fun registerSerializer(
        _serializers: SerializersModuleBuilder.() -> Unit
    ) {
        val class2ContextualFactoryField = _serializersModule::class.java.getDeclaredField("class2ContextualFactory")

        class2ContextualFactoryField.isAccessible = true

        val class2ContextualFactory = class2ContextualFactoryField.get(_serializersModule) as MutableMap<KClass<*>, Any?>

        val serializersModule = SerializersModule { _serializers() }

        val _class2ContextualFactory = serializersModule::class.java.getDeclaredField("class2ContextualFactory")

        _class2ContextualFactory.isAccessible = true

        val class2ContextualProvider = _class2ContextualFactory.get(serializersModule) as Map<KClass<*>, Any?>

        class2ContextualProvider.forEach { (key, value) ->
            class2ContextualFactory[key] = value
        }

        class2ContextualFactoryField.set(_serializersModule, class2ContextualFactory)
    }

    /**
     * Class deserializers
     */

    @ExperimentalSerializationApi
    fun decodeFromString(
        kClass: KClass<*>,
        string: String?
    ) = _json.decodeFromString(
        fetchSerializerForKClass(kClass),
        string ?: throw IllegalArgumentException()
    )

    inline fun <reified T> encodeToString(
        kClass: KClass<*>,
        t: T?
    ): String {
        val result = Class.forName("kotlinx.serialization.json.internal.JsonStringBuilder").constructors[0].newInstance()

        try {
            val writeMode = Class.forName("kotlinx.serialization.json.internal.WriteMode")
            val encoder = Class.forName("kotlinx.serialization.json.internal.StreamingJsonEncoder").constructors[0].newInstance(
                result,
                _json,
                (writeMode.enumConstants as Array<Enum<*>>).first { it.name == "OBJ" },
                arrayOfNulls<JsonEncoder>((writeMode.enumConstants as Array<Enum<*>>).size)
            )
            val encodeSerializableValue = encoder::class.java.getDeclaredMethod(
                "encodeSerializableValue",
                SerializationStrategy::class.java,
                Any::class.java
            )

            encodeSerializableValue.isAccessible = true

            encodeSerializableValue.invoke(
                encoder,
                fetchSerializerForKClass(kClass),
                t
            )

            return result.toString()
        } finally {
            val release = result::class.java.getDeclaredMethod("release")

            release.isAccessible = true

            release.invoke(result)
        }
    }

    class DeserializerNotFoundException(
        message: String
    ) : NullPointerException(message)

    /**
     * Class serializers
     */

    @ExperimentalSerializationApi
    fun fetchSerializerForKClass(
        kClass: KClass<*>
    ) = _json.serializersModule.getContextual(kClass) ?: throw DeserializerNotFoundException("Cannot find an deserializer for kclass $kClass")

}

fun <T> Json.encodeToString(serializer: KSerializer<out Any>, value: T): String {
    return ""
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

    final override fun deserialize(
        decoder: Decoder
    ): T = deserialize(
        decoder as JsonDecoder
    )

}

fun JsonElement.asString(): String = this.jsonPrimitive.content

fun JsonElement.asInt(): Int = this.jsonPrimitive.int

fun JsonElement.isInt() = this.jsonPrimitive.content.toIntOrNull() != null

fun JsonElement.asDouble(): Double = this.jsonPrimitive.double

fun JsonElement.isDouble() = this.jsonPrimitive.content.toDoubleOrNull() != null

fun JsonElement.asFloat(): Float = this.jsonPrimitive.float

fun JsonElement.isFloat() = this.jsonPrimitive.content.toFloatOrNull() != null

fun JsonElement.asLong(): Long = this.jsonPrimitive.long

fun JsonElement.isLong() = this.jsonPrimitive.content.toLongOrNull() != null

fun JsonElement.asBoolean(): Boolean = this.jsonPrimitive.boolean

fun JsonElement.isBoolean() = this.jsonPrimitive.content.toBooleanStrictOrNull() != null

fun <T: Enum<T>> JsonElement.asEnum(
    kClass: KClass<T>
): T? = Enums.getIfPresent(
    kClass.java,
    this.asString()
).orNull()

fun JsonElement.asJsonObject(): JsonObject = this.jsonObject

fun JsonElement.asJsonArray(): JsonArray = this.jsonArray