package com.int531.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val config = environment.config.config("database")

    val host = config.property("host").getString()
    val port = config.property("port").getString()
    val name = config.property("name").getString()
    val user = config.property("user").getString()
    val password = config.property("password").getString()

    val jdbcUrl = "jdbc:mysql://$host:$port/$name"

    Database.connect(
        url = jdbcUrl,
        driver = "com.mysql.cj.jdbc.Driver",
        user = user,
        password = password
    )
}
