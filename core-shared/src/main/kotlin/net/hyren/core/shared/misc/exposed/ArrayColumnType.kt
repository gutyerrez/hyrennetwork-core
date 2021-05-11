package net.hyren.core.shared.misc.exposed

import net.hyren.core.shared.CoreConstants
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.SQLFeatureNotSupportedException
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
fun <T> Table.array(
    name: String,
    kClass: KClass<*>
): Column<Array<T>> = registerColumn(name, ArrayColumnType(kClass))

class ArrayColumnType(
    private val kClass: KClass<*>
) : ColumnType() {
    override fun sqlType() = "JSON"

    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is java.sql.Array) {
            if (value.array === null) error("Cannot read null array")

            return value.array
        } else if (value is String) {
            return CoreConstants.JACKSON.readValue(value, kClass.java)
        } else if (value is Array<*>) {
            return value
        }

        throw SQLFeatureNotSupportedException("Array does not support for this database")
    }

    override fun setParameter(
        stmt: PreparedStatementApi,
        index: Int,
        value: Any?
    ) {
        super.setParameter(stmt, index, value.let {
            CoreConstants.JACKSON.writerWithDefaultPrettyPrinter().writeValueAsString(value)
        })
    }

}
