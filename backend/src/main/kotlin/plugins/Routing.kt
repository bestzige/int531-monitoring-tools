package com.int531.plugins

import com.int531.routes.textRoutes
import com.int531.services.TextService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val textService: TextService by inject()

    routing {
        textRoutes(textService)
    }
}
