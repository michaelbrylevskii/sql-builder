package me.michaelbrylevskii.sql.builder.writer

import me.michaelbrylevskii.sql.builder.model.expression.*
import me.michaelbrylevskii.sql.builder.model.predicate.ExpressionPredicate
import me.michaelbrylevskii.sql.builder.model.predicate.LogicalPredicate
import me.michaelbrylevskii.sql.builder.model.predicate.Predicate
import me.michaelbrylevskii.sql.builder.model.query.Query
import me.michaelbrylevskii.sql.builder.model.query.SelectQuery
import me.michaelbrylevskii.sql.builder.model.selection.AllSelection
import me.michaelbrylevskii.sql.builder.model.selection.ExpressionSelection
import me.michaelbrylevskii.sql.builder.model.selection.Selection
import me.michaelbrylevskii.sql.builder.model.source.Container
import me.michaelbrylevskii.sql.builder.model.source.Source
import me.michaelbrylevskii.sql.builder.model.source.Table

open class ConfigurableQueryWriter(

) : QueryWriter {

    override fun write(query: Query): String {
        return buildString {
            when (query) {
                is SelectQuery -> appendSelectQuery(query)
                else -> TODO("Not yet implemented")
            }
        }
    }

    protected fun <A : Appendable> A.appendSelectQuery(
        query: SelectQuery
    ): A = apply {
        appendSelectStatement(query)
        appendFromStatement(query)
        appendWhereStatement(query)
    }

    // ------------------------
    // --- Select Statement ---
    // ------------------------

    protected fun <A : Appendable> A.appendSelectStatement(
        query: SelectQuery
    ): A = apply {
        append("SELECT ")
        appendList(query.selections) { selection ->
            appendSelection(selection)
        }
    }

    protected fun <A : Appendable> A.appendSelection(
        selection: Selection
    ): A = apply {
        when (selection) {
            is AllSelection -> appendAllSelection(selection)
            is ExpressionSelection -> appendExpressionSelection(selection)
            else -> TODO("Not yet implemented")
        }
    }

    protected fun <A : Appendable> A.appendAllSelection(
        selection: AllSelection
    ): A = apply {
        val alias = selection.source?.alias
        if (alias != null) {
            append(alias)
            append(".")
        }
        append("*")
    }

    protected fun <A : Appendable> A.appendExpressionSelection(
        selection: ExpressionSelection
    ): A = apply {
        appendExpression(selection.expression)
        if (selection.asColumn != null) {
            append(" \"")
            append(selection.asColumn)
            append("\"")
        }
    }

    // ----------------------
    // --- From Statement ---
    // ----------------------

    protected fun <A : Appendable> A.appendFromStatement(
        query: SelectQuery
    ): A = apply {
        if (query.sources.isEmpty()) return@apply

        append(" FROM ")
        appendList(query.sources) { source ->
            appendSource(source)
        }
    }

    protected fun <A : Appendable> A.appendSource(
        source: Source
    ): A = apply {
        appendContainer(source.container)
        if (source.alias != null) {
            append(" \"")
            append(source.alias)
            append("\"")
        }
    }

    protected fun <A : Appendable> A.appendContainer(
        container: Container
    ): A = apply {
        when (container) {
            is Table -> appendTableContainer(container)
            is SelectQuery -> appendSelectQueryContainer(container)
            else -> TODO("Not yet implemented")
        }
    }

    protected fun <A : Appendable> A.appendTableContainer(
        container: Table
    ): A = apply {
        append(container.name)
    }

    protected fun <A : Appendable> A.appendSelectQueryContainer(
        container: SelectQuery
    ): A = apply {
        append("(")
        appendSelectQuery(container)
        append(")")
    }

    // -----------------------
    // --- Where Statement ---
    // -----------------------

    protected fun <A : Appendable> A.appendWhereStatement(
        query: SelectQuery
    ): A = apply {
        if (query.where == null) return@apply

        append(" WHERE ")
        appendPredicate(query.where)
    }

    protected fun <A : Appendable> A.appendPredicate(
        predicate: Predicate
    ): A = apply {
        when (predicate) {
            is ExpressionPredicate -> appendExpressionPredicate(predicate)
            is LogicalPredicate -> appendLogicalPredicate(predicate)
            else -> TODO("Not yet implemented")
        }
    }

    protected fun <A : Appendable> A.appendExpressionPredicate(
        predicate: ExpressionPredicate
    ): A = apply {
        if (predicate.args.size == 2) {
            val firstArg = predicate.args[0]
            val secondArg = predicate.args[1]
            appendExpression(firstArg)
            append(" ")
            append(predicate.operator)
            append(" ")
            appendExpression(secondArg)
        } else TODO("Not yet implemented")
    }

    protected fun <A : Appendable> A.appendLogicalPredicate(
        predicate: LogicalPredicate
    ): A = apply {
        when {
            predicate.operator == "NOT" && predicate.args.size == 1 -> {
                append("NOT (")
                appendPredicate(predicate.args[0])
                append(")")
            }
            predicate.operator == "AND" && predicate.args.size >= 2 -> {
                appendList(predicate.args, delimiter = " AND ") { arg ->
                    val needWrap = arg is LogicalPredicate && arg.operator == "OR"
                    if (needWrap) {
                        append("(")
                    }
                    appendPredicate(arg)
                    if (needWrap) {
                        append(")")
                    }
                }
            }
            predicate.operator == "OR" && predicate.args.size >= 2 -> {
                appendList(predicate.args, delimiter = " OR ") { arg ->
                    val needWrap = arg is LogicalPredicate && arg.operator == "AND"
                    if (needWrap) {
                        append("(")
                    }
                    appendPredicate(arg)
                    if (needWrap) {
                        append(")")
                    }
                }
            }
            else -> TODO("Not yet implemented")
        }
    }

    // ------------------
    // --- Expression ---
    // ------------------

    protected fun <A : Appendable> A.appendExpression(
        expression: Expression
    ): A = apply {
        when (expression) {
            is ColumnExpression -> appendColumnExpression(expression)
            is ParameterExpression -> appendParameterExpression(expression)
            is LiteralExpression -> appendLiteralExpression(expression)
            is OperatorExpression -> appendOperatorExpression(expression)
            is FunctionExpression -> appendFunctionExpression(expression)
            is SelectQuery -> appendSelectQueryExpression(expression)
            else -> TODO("Not yet implemented")
        }
    }

    protected fun <A : Appendable> A.appendColumnExpression(
        expression: ColumnExpression
    ): A = apply {
        val alias = expression.source?.alias ?: (expression.source?.container as? Table)?.name
        if (alias != null) {
            append(alias)
            append(".")
        }
        append(expression.name)
    }

    protected fun <A : Appendable> A.appendParameterExpression(
        expression: ParameterExpression
    ): A = apply {
        append(":")
        append(expression.parameter)
    }

    protected fun <A : Appendable> A.appendLiteralExpression(
        expression: LiteralExpression
    ): A = apply {
        when (val value = expression.value) {
            null -> append("NULL")
            is Boolean -> append(if (value) "TRUE" else "FALSE")
            is Number -> append(value.toString())
            is String -> {
                append("'")
                append(value)
                append("'")
            }
            else -> TODO("Not yet implemented")
        }
    }

    protected fun <A : Appendable> A.appendOperatorExpression(
        expression: OperatorExpression
    ): A = apply {
        if (expression.args.size == 2) {
            val firstArg = expression.args[0]
            val secondArg = expression.args[1]
            appendExpression(firstArg)
            append(" ")
            append(expression.operator)
            append(" ")
            appendExpression(secondArg)
        } else TODO("Not yet implemented")
    }

    protected fun <A : Appendable> A.appendFunctionExpression(
        expression: FunctionExpression
    ): A = apply {
        append(expression.name)
        append("(")
        appendList(expression.args) { argExpression ->
            appendExpression(argExpression)
        }
        append(")")
    }

    protected fun <A : Appendable> A.appendSelectQueryExpression(
        expression: SelectQuery
    ): A = apply {
        append("(")
        appendSelectQuery(expression)
        append(")")
    }

    // --------------
    // --- Common ---
    // --------------

    protected fun <A : Appendable, T : Any> A.appendList(
        items: Iterable<T>,
        delimiter: String = ", ",
        appender: A.(T) -> Unit = { append(it.toString()) }
    ): A = apply {
        val iterator = items.iterator()
        var hasNext = iterator.hasNext()

        while (hasNext) {
            appender(iterator.next())
            hasNext = iterator.hasNext()
            if (hasNext) {
                append(delimiter)
            }
        }
    }
}
