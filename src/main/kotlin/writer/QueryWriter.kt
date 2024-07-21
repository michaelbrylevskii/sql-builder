package me.michaelbrylevskii.sql.builder.writer

import me.michaelbrylevskii.sql.builder.model.query.Query

interface QueryWriter {
    fun write(query: Query): String
}
