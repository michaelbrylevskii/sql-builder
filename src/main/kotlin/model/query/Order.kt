package me.michaelbrylevskii.sql.builder.model.query

import me.michaelbrylevskii.sql.builder.model.expression.Expression

data class Order(
    val expression: Expression,
    val direction: Direction
) {
    enum class Direction {
        ASC, DESC
    }
}
