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

        select("some_column")
        select(column = "status")
        select(column = "status", asColumn = "account_status")
        select(column = "id", source = accounts)
        select(column = "id", source = accounts, asColumn = "account_id")

        select(expression = accounts.column("amount"))
        select(expression = accounts.column("amount"), asColumn = "amount_0")
//        select(expression = accounts.column("amount") / 1000, asColumn = "amount_K")
//        select(expression = accounts.column("amount") / 1000000, asColumn = "amount_M")

        where(predicate = accounts.column("amount") eq literal(10))
        where(predicate = accounts.column("status") eq literal("ACTIVE"))
        where(
            (transactions.column("accountNumber") eq accounts.column("number"))
                    and (not(transactions.column("accountNumber") eq literal("0000")))
                    and ((transactions.column("unit") eq literal("RU"))
                    or (transactions.column("unit") eq literal("KZ")))
        )
    }

    val writer = ConfigurableQueryWriter()

    val sql = writer.write(query)

    println("SQL:")
    println(sql)
    println()
}

fun SelectQueryDsl.selectTypicalColumns() {
    select("column_1")
    select("column_2")
    select("column_3")
}
