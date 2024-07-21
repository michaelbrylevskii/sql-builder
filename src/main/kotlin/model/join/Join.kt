package me.michaelbrylevskii.sql.builder.model.join

import me.michaelbrylevskii.sql.builder.model.source.Source

interface Join {
    val source: Source
}
