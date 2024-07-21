package me.michaelbrylevskii.sql.builder.model.order

import me.michaelbrylevskii.sql.builder.model.expression.Expression

data class Order(
    val expression: Expression,
    val direction: Direction
)
