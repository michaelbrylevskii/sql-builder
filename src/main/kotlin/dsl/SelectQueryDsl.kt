package me.michaelbrylevskii.sql.builder.dsl

import me.michaelbrylevskii.sql.builder.model.expression.ColumnExpression
import me.michaelbrylevskii.sql.builder.model.expression.Expression
import me.michaelbrylevskii.sql.builder.model.predicate.Predicate
import me.michaelbrylevskii.sql.builder.model.query.Order
import me.michaelbrylevskii.sql.builder.model.query.SelectQuery
import me.michaelbrylevskii.sql.builder.model.query.selection.AllSelection
import me.michaelbrylevskii.sql.builder.model.query.selection.ExpressionSelection
import me.michaelbrylevskii.sql.builder.model.query.selection.Selection
import me.michaelbrylevskii.sql.builder.model.source.Container
import me.michaelbrylevskii.sql.builder.model.source.Join
import me.michaelbrylevskii.sql.builder.model.source.Source
import me.michaelbrylevskii.sql.builder.model.source.Table

class SelectQueryDsl {
    private val selections: MutableList<Selection> = arrayListOf()
    private val sources: MutableList<Source> = arrayListOf()
    private val joins: MutableList<Join> = arrayListOf()
    private val where: Predicate? = null
    private val groupBy: MutableList<Expression> = arrayListOf()
    private val having: Predicate? = null
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


    fun Source.column(name: String): ColumnExpression =
        ColumnExpression(name = name, source = this)
}
