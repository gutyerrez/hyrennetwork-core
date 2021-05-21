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
inline fun <reified T> Table.json(
    name: String,
    kClass: KClass<*>
): Column<T> = registerColumn(name, JsonColumnType(kClass))

class JsonColumnType(
    private val kClass: KClass<*>
) : ColumnType() {

    override fun sqlType() = "JSON"

    override fun valueFromDB(
        value: Any
    ): Any = when (value) {
        is PGobject -> KJson.decodeFromString(kClass, value.value)
        is String -> KJson.decodeFromString(kClass, value)
        else -> throw SQLFeatureNotSupportedException("Json does not support for this database")
    }

    override fun setParameter(
        stmt: PreparedStatementApi,
        index: Int,
        value: Any?
    ) {
        super.setParameter(stmt, index, value.let {
            PGobject().apply {
                this.type = sqlType()
                this.value = if (value == null) null else KJson.encodeToString(kClass, value)
            }
        })
    }

}
