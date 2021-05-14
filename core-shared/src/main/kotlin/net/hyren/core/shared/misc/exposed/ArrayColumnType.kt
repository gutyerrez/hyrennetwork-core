package net.hyren.core.shared.misc.exposed

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.misc.punish.durations.PunishDuration
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.SQLFeatureNotSupportedException
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
inline fun <reified T> Table.array(
    name: String,
    noinline deserialize: (decoder: Decoder) -> Array<T>? = { null }
): Column<Array<T>> = registerColumn(
    name,
    ArrayColumnType(
        T::class,
        deserialize
    )
)

class ArrayColumnType<T>(
    private val kClass: KClass<*>,
    private val deserialize: (decoder: Decoder) -> Array<T>?
) : ColumnType() {

    override fun sqlType() = "longtext"

    @InternalSerializationApi
    @ExperimentalSerializationApi
    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is java.sql.Array) {
            if (value.array === null) {
                error("Cannot read null array")
            }

            return value.array
        } else if (value is String) {
            return if (CoreProvider.application.applicationType != ApplicationType.SERVER_TESTS) {
                Json.decodeFromString(object : DeserializationStrategy<Array<T>> {
                    override val descriptor: SerialDescriptor = ContextualSerializer(
                        kClass,
                        null,
                        emptyArray()
                    ).descriptor

                    override fun deserialize(
                        decoder: Decoder
                    ): Array<T> {
                        val jsonArray = (decoder as JsonDecoder).decodeJsonElement().jsonArray

                        val array = java.lang.reflect.Array.newInstance(
                            kClass.java,
                            jsonArray.size
                        ) as Array<T>

                        var _index = 0

                        jsonArray.forEachIndexed { index, jsonElement ->
                            when (kClass) {
                                PunishDuration::class -> {
                                    array[_index] = Json.decodeFromJsonElement<PunishDuration>(jsonElement) as T
                                }
                                else -> this@ArrayColumnType.deserialize.invoke(decoder)
                            }

                            _index++
                        }

                        return array
                    }
                }, value)
            } else return Any()
        } else if (value is Array<*>) {
            return value
        }

        throw SQLFeatureNotSupportedException("Array does not support for this database")
    }

    override fun setParameter(
        stmt: PreparedStatementApi, index: Int, value: Any?
    ) {
        super.setParameter(stmt, index, value.let {
            Json.encodeToString(it)
        })
    }
}
