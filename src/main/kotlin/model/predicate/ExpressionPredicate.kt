package me.michaelbrylevskii.sql.builder.model.predicate

import me.michaelbrylevskii.sql.builder.model.expression.Expression

data class ExpressionPredicate(
    val operator: String,
    val args: List<Expression>
) : Predicate
