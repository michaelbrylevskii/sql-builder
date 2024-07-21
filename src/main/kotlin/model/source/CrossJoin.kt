package me.michaelbrylevskii.sql.builder.model.source

data class CrossJoin(
    override val source: Source
) : Join
