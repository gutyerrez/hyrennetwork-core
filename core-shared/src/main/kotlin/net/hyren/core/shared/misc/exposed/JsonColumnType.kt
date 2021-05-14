package net.hyren.core.shared.misc.exposed

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.SQLFeatureNotSupportedException
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
inline fun <reified T> Table.json(
    name: String,
    noinline deserialize: (decoder: Decoder) -> T? = { null }
): Column<T> = registerColumn(
    name,
    JsonColumnType(
        T::class,
        deserialize
    )
)

class JsonColumnType<T>(
    private val kClass: KClass<*>,
    private val deserialize: (decoder: Decoder) -> T?
) : ColumnType() {

    override fun sqlType() = "longtext"

    override fun valueFromDB(
        value: Any
    ): Any = when (kClass) {
        else -> {
            value as String

            Json.decodeFromString(object : DeserializationStrategy<T> {
                override val descriptor: SerialDescriptor = ContextualSerializer(
                    kClass,
                    null,
                    emptyArray()
                ).descriptor

                override fun deserialize(
                    decoder: Decoder
                ): T {
                    return this@JsonColumnType.deserialize.invoke(decoder) ?: throw SQLFeatureNotSupportedException("Object does not support for this database")
                }
            }, value) as Any
        }
    }

    override fun setParameter(
        stmt: PreparedStatementApi, index: Int, value: Any?
    ) {
        super.setParameter(stmt, index, value.let {
            Json.encodeToString(it)
        })
    }
}
