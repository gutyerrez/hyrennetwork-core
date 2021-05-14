package net.hyren.core.shared.misc.exposed

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.SQLFeatureNotSupportedException

/**
 * @author Gutyerrez
 */
fun <T> Table.array(
    name: String
): Column<Array<T>> = registerColumn(name, ArrayColumnType())

class ArrayColumnType : ColumnType() {

    override fun sqlType() = "longtext"

    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is java.sql.Array) {
            if (value.array === null) {
                error("Cannot read null array")
            }

            return value.array
        } else if (value is String) {
            return Json.decodeFromString<JsonArray>(value)
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
