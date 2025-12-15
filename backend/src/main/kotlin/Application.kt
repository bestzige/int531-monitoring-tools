package com.int531

import com.int531.plugins.configureAdministration
import com.int531.plugins.configureCORS
import com.int531.plugins.configureDatabases
import com.int531.plugins.configureKoin
import com.int531.plugins.configureMonitoring
import com.int531.plugins.configureRouting
import com.int531.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMonitoring()
    configureDatabases()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureCORS()
    configureRouting()
}
