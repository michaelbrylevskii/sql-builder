package me.michaelbrylevskii.sql.builder.model.join

import me.michaelbrylevskii.sql.builder.model.predicate.Predicate
import me.michaelbrylevskii.sql.builder.model.source.Source

data class ConditionalJoin(
    override val source: Source,
    val on: Predicate,
    val type: Type
) : Join {
    enum class Type {
        INNER, LEFT, RIGHT, FULL
    }
}
