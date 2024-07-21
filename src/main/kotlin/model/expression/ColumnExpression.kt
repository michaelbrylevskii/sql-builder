package me.michaelbrylevskii.sql.builder.model.expression

import me.michaelbrylevskii.sql.builder.model.source.Source

data class ColumnExpression(
    val name: String,
    val source: Source?
) : Expression
