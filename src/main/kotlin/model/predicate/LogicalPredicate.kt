package me.michaelbrylevskii.sql.builder.model.predicate

data class LogicalPredicate(
    val operator: String,
    val args: List<Predicate>
) : Predicate
