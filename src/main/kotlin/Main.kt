package me.michaelbrylevskii.sql.builder

import me.michaelbrylevskii.sql.builder.dsl.SelectQueryDsl
import me.michaelbrylevskii.sql.builder.dsl.selectQuery
import me.michaelbrylevskii.sql.builder.writer.ConfigurableQueryWriter

fun main() {
    println("Hello World!")

    val query = selectQuery {
        val accounts = from(table = "accounts", alias = "a")
        val transactions = from(table = "transactions", alias = "t")

        selectTypicalColumns()

        selectAll()
        selectAll(accounts)

        select(column = "status")
        select(column = "status", asColumn = "account_status")
        select(column = "id", source = accounts)
        select(column = "id", source = accounts, asColumn = "account_id")

        select(expression = accounts.column("amount"))
        select(expression = accounts.column("amount"), asColumn = "amount_0")
//        select(expression = accounts.column("amount") / 1000, asColumn = "amount_K")
//        select(expression = accounts.column("amount") / 1000000, asColumn = "amount_M")

        //where(accounts.column("amount").greaterThan(literal(10)))
    }

    val writer = ConfigurableQueryWriter()

    val sql = writer.write(query)

    println("SQL:")
    println(sql)
    println()
}

fun SelectQueryDsl.selectTypicalColumns() {
    select(column = "column_1")
    select(column = "column_2")
    select(column = "column_3")
}
