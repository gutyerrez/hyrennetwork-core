package com.redefantasy.core.shared.misc.exposed

import com.redefantasy.core.shared.CoreConstants
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject
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
    override fun sqlType() = "JSONB"

    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is PGobject) {
            if (value.value === null) error("Cannot read null array")

            return CoreConstants.JACKSON.readValue(value.value, kClass.java)
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
            PGobject().apply {
                this.type = sqlType()
                this.value = if (value === null) null else CoreConstants.JACKSON.writeValueAsString(value)
            }
        })
    }

}

class AnyOp(
    val expr1: Expression<*>,
    val expr2: Expression<*>
) : Op<Boolean>() {

    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        if (expr2 is OrOp) {
            queryBuilder.append("(").append(expr2).append(")")
        } else {
            queryBuilder.append(expr2)
        }

        queryBuilder.append(" = ANY (")

        if (expr1 is OrOp) {
            queryBuilder.append("(").append(expr1).append(")")
        } else {
            queryBuilder.append(expr1)
        }

        queryBuilder.append(")")
    }

}

class ContainsOp(
    expr1: Expression<*>,
    expr2: Expression<*>
) : ComparisonOp(expr1, expr2, "@>")

infix fun <T, S> ExpressionWithColumnType<T>.any(t: S): Op<Boolean> {
    if (t == null) {
        return IsNullOp(this)
    }

    return AnyOp(this, QueryParameter(t, columnType))
}

infix fun <T, S> ExpressionWithColumnType<T>.contains(array: Array<in S>): Op<Boolean> = ContainsOp(this, QueryParameter(array, columnType))