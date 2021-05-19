package net.hyren.core.shared.misc.exposed

import net.hyren.core.shared.misc.json.KJson
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import java.sql.SQLFeatureNotSupportedException
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
inline fun <reified T> Table.array(
    name: String,
    kClass: KClass<*>
): Column<Array<T>> = registerColumn(name, ArrayColumnType<T>(kClass))

class ArrayColumnType<T>(
    private val kClass: KClass<*>
) : ColumnType() {

    override fun sqlType() = "JSON"

    override fun valueFromDB(
        value: Any
    ): Any = when (value) {
        is PGobject -> KJson.decodeFromString(kClass, value.value)
        is String -> KJson.decodeFromString(kClass, value)
        is Array<*> -> value
        else -> throw SQLFeatureNotSupportedException("Array does not support for this database")
    }

    override fun notNullValueToDB(value: Any) = when (value) {
        is Array<*> -> KJson.encodeToString(value)
        else -> throw SQLFeatureNotSupportedException("Cannot find serializer for ${value::class.qualifiedName}")
    }

}
