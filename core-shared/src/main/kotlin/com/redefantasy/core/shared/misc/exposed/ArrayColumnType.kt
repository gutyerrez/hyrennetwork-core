package com.redefantasy.core.shared.misc.exposed

import com.redefantasy.core.shared.CoreConstants
import org.jetbrains.exposed.sql.*
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

    override fun valueToDB(
        value: Any?
    ): Any? {
        if (value is Array<*>) {
            return CoreConstants.JACKSON.writeValueAsString(value)
        } else return null
    }

    override fun valueFromDB(
        value: Any
    ): Any {
        if (value is PGobject) {
            if (value.value === null) error("Cannot read null array")

            return CoreConstants.JACKSON.readValue(value.value, kClass.java)
        }

        if (value is Array<*>) {
            return value
        }

        throw SQLFeatureNotSupportedException("Array does not support for this database")
    }

    override fun notNullValueToDB(
        value: Any
    ): Any {
        if (value is Array<*>) {
            if (value.isEmpty()) return "'[]'"

            return CoreConstants.JACKSON.writeValueAsString(value)
        } else throw SQLFeatureNotSupportedException("Can't create non null array for $value")
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