package net.hyren.core.shared.misc.exposed

import com.google.gson.JsonObject
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
fun <T> Table.json(
	name: String,
	kClass: KClass<*>
): Column<T> = registerColumn(name, JsonColumnType(kClass))

class JsonColumnType(
	private val kClass: KClass<*>
) : ColumnType() {
	override fun sqlType() = "JSON"

	override fun valueFromDB(
		value: Any
	): Any {
		if (value is JsonObject) {
			return CoreConstants.JACKSON.readValue(value.asString, kClass.java)
		} else if (value is String) {
			return CoreConstants.JACKSON.readValue(value, kClass.java)
		}

		throw SQLFeatureNotSupportedException("Object does not support for this database")
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
