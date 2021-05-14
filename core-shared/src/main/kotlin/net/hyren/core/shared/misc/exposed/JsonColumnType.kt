package net.hyren.core.shared.misc.exposed

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.SQLFeatureNotSupportedException

/**
 * @author Gutyerrez
 */
inline fun <reified T> Table.json(
    name: String
): Column<T> = registerColumn(name, JsonColumnType<T>())

class JsonColumnType<T> : ColumnType() {

    override fun sqlType() = "longtext"

    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is JsonElement) {
            return (value.jsonObject as T) as Any
        } else if (value is String) {
            return Json.decodeFromString(value)
        }

        throw SQLFeatureNotSupportedException("Object does not support for this database")
    }

    override fun setParameter(
        stmt: PreparedStatementApi, index: Int, value: Any?
    ) {
        super.setParameter(stmt, index, value.let {
            Json.encodeToString(it)
        })
    }
}
