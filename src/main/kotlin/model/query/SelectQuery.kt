package me.michaelbrylevskii.sql.builder.model.query

import me.michaelbrylevskii.sql.builder.model.expression.Expression
import me.michaelbrylevskii.sql.builder.model.predicate.Predicate
import me.michaelbrylevskii.sql.builder.model.order.Order
import me.michaelbrylevskii.sql.builder.model.selection.Selection
import me.michaelbrylevskii.sql.builder.model.source.Container
import me.michaelbrylevskii.sql.builder.model.join.Join
import me.michaelbrylevskii.sql.builder.model.source.Source

data class SelectQuery(
    val selections: List<Selection>,
    val sources: List<Source>,
    val joins: List<Join>,
    val where: Predicate?,
    val groupBy: List<Expression>,
    val having: Predicate?,
    val orderBy: List<Order>,
) : Query, Container, Expression
