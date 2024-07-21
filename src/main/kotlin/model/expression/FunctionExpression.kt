package me.michaelbrylevskii.sql.builder.model.expression

data class FunctionExpression(
    val name: String,
    val args: List<Expression>
) : Expression
