package me.michaelbrylevskii.sql.builder.dsl

import me.michaelbrylevskii.sql.builder.model.expression.ColumnExpression
import me.michaelbrylevskii.sql.builder.model.expression.Expression
import me.michaelbrylevskii.sql.builder.model.expression.LiteralExpression
import me.michaelbrylevskii.sql.builder.model.join.Join
import me.michaelbrylevskii.sql.builder.model.order.Order
import me.michaelbrylevskii.sql.builder.model.predicate.ExpressionPredicate
import me.michaelbrylevskii.sql.builder.model.predicate.LogicalPredicate
import me.michaelbrylevskii.sql.builder.model.predicate.Predicate
import me.michaelbrylevskii.sql.builder.model.query.SelectQuery
import me.michaelbrylevskii.sql.builder.model.selection.AllSelection
import me.michaelbrylevskii.sql.builder.model.selection.ExpressionSelection
import me.michaelbrylevskii.sql.builder.model.selection.Selection
import me.michaelbrylevskii.sql.builder.model.source.Container
import me.michaelbrylevskii.sql.builder.model.source.Source
import me.michaelbrylevskii.sql.builder.model.source.Table

class SelectQueryDsl {
    private val selections: MutableList<Selection> = arrayListOf()
    private val sources: MutableList<Source> = arrayListOf()
    private val joins: MutableList<Join> = arrayListOf()
    private var where: Predicate? = null
    private val groupBy: MutableList<Expression> = arrayListOf()
    private var having: Predicate? = null
    private val orderBy: MutableList<Order> = arrayListOf()

    fun select(
        selections: Collection<Selection>
    ) = this.selections.addAll(selections)

    fun <T : Selection> select(
        selection: T
    ): T = selection.also { selections.add(it) }

    fun select(
        expression: Expression,
        asColumn: String? = null
    ): ExpressionSelection = select(
        selection = ExpressionSelection(expression = expression, asColumn = asColumn)
    )

    fun select(
        column: String,
        source: Source? = null,
        asColumn: String? = null
    ): ExpressionSelection = select(
        expression = ColumnExpression(name = column, source = source),
        asColumn = asColumn
    )

    fun selectAll(
        source: Source? = null
    ): AllSelection = select(
        selection = AllSelection(source = source)
    )

    fun from(
        sources: Collection<Source>
    ) = this.sources.addAll(sources)

    fun from(
        source: Source
    ): Source = source.also { sources.add(it) }

    fun from(
        container: Container,
        alias: String? = null
    ): Source = from(Source(container = container, alias = alias))

    fun from(
        table: String,
        alias: String? = null
    ): Source = from(Table(name = table), alias = alias)

    fun where(
        predicate: Predicate
    ): Predicate {
        val currentWhere = where
        if (currentWhere == null) {
            this.where = predicate
        } else if (currentWhere is LogicalPredicate && currentWhere.operator == "AND") {
            this.where = LogicalPredicate(operator = "AND", args = currentWhere.args + predicate)
        } else {
            this.where = LogicalPredicate(operator = "AND", args = listOf(currentWhere, predicate))
        }
        return this.where!!
    }

    fun build(): SelectQuery {
        if (selections.isEmpty()) {
            selectAll()
        }
        return SelectQuery(
            selections = selections,
            sources = sources,
            joins = joins,
            where = where,
            groupBy = groupBy,
            having = having,
            orderBy = orderBy
        )
    }

    fun literal(value: Any?): LiteralExpression =
        LiteralExpression(value = value)

    fun Source.column(name: String): ColumnExpression =
        ColumnExpression(name = name, source = this)

    infix fun Expression.eq(expression: Expression): ExpressionPredicate =
        ExpressionPredicate(operator = "=", args = listOf(this, expression))

    infix fun Expression.notEq(expression: Expression): ExpressionPredicate =
        ExpressionPredicate(operator = "!=", args = listOf(this, expression))

    infix fun not(predicate: Predicate): LogicalPredicate =
        LogicalPredicate(operator = "NOT", args = listOf(predicate))

    infix fun Predicate.and(predicate: Predicate): LogicalPredicate =
        LogicalPredicate(operator = "AND", args = listOf(this, predicate))

    infix fun Predicate.or(predicate: Predicate): LogicalPredicate =
        LogicalPredicate(operator = "OR", args = listOf(this, predicate))
}
