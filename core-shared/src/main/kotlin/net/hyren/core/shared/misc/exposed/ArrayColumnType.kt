package net.hyren.core.shared.misc.exposed

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
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
    name: String
): Column<Array<T>> = registerColumn(name, ArrayColumnType<T>(T::class))

class ArrayColumnType<T>(
    private val kClass: KClass<*>
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
            return Json.decodeFromString(object : DeserializationStrategy<Array<T>> {
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
                        val a = jsonElement.jsonObject as T

                        println(a)

                        _index++
                    }

                    return array
                }
            }, value)
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
