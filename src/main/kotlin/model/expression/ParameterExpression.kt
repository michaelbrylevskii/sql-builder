package me.michaelbrylevskii.sql.builder.model.expression

data class ParameterExpression(
    val parameter: String
) : Expression
