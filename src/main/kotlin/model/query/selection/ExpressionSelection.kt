package me.michaelbrylevskii.sql.builder.model.query.selection

import me.michaelbrylevskii.sql.builder.model.expression.Expression

data class ExpressionSelection(
    val expression: Expression,
    val asColumn: String?
) : Selection
