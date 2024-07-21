package me.michaelbrylevskii.sql.builder.dsl

import me.michaelbrylevskii.sql.builder.model.query.SelectQuery

fun selectQuery(configurer: SelectQueryDsl.() -> Unit): SelectQuery {
    val dsl = SelectQueryDsl()
    dsl.configurer()
    return dsl.build()
}
