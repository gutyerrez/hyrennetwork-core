package net.hyren.core.shared.misc.exposed

import org.jetbrains.exposed.sql.*

/**
 * @author Gutyerrez
 */
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
