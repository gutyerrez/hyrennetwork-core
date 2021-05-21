package net.hyren.core.shared.misc.exposed

import net.hyren.core.shared.misc.json.KJson
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject
import java.sql.SQLFeatureNotSupportedException
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
inline fun <reified T> Table.array(
    name: String,
    kClass: KClass<*>
): Column<Array<T>> = registerColumn(name, ArrayColumnType(kClass))

class ArrayColumnType(
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

    override fun setParameter(
        stmt: PreparedStatementApi,
        index: Int,
        value: Any?
    ) {
        super.setParameter(stmt, index, value?.let {
            PGobject().apply {
                this.type = sqlType()
                this.value = KJson.encodeToString(kClass, value)
            }
        })
    }

}
