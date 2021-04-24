package com.redefantasy.core.shared.misc.exposed

import com.redefantasy.core.shared.CoreConstants
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
		if (value is PGobject) {
			if (value.value === null) error("Cannot read null object")

			return CoreConstants.JACKSON.readValue(value.value, kClass.java)
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
			PGobject().apply {
				this.type = sqlType()
				this.value = if (value === null) null else {
					CoreConstants.JACKSON.writerWithDefaultPrettyPrinter().writeValueAsString(value)
				}
			}
		})
	}

}
