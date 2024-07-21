package me.michaelbrylevskii.sql.builder.model.expression

data class OperatorExpression(
    val operator: String,
    val args: List<Expression>
) : Expression
