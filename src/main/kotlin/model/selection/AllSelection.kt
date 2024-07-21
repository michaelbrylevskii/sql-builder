package me.michaelbrylevskii.sql.builder.model.selection

import me.michaelbrylevskii.sql.builder.model.source.Source

data class AllSelection(
    val source: Source? = null
) : Selection
