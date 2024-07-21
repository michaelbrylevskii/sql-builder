package me.michaelbrylevskii.sql.builder.model.join

import me.michaelbrylevskii.sql.builder.model.source.Source

data class CrossJoin(
    override val source: Source
) : Join
