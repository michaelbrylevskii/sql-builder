package me.michaelbrylevskii.sql.builder.model.source

import me.michaelbrylevskii.sql.builder.model.predicate.Predicate

data class ConditionalJoin(
    override val source: Source,
    val on: Predicate,
    val type: Type
) : Join {
    enum class Type {
        INNER, LEFT, RIGHT, FULL
    }
}
