package com.int531.routes

import com.int531.services.TextService
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put

fun Route.textRoutes(textService: TextService) {
    get("/texts") {
        val texts = textService.getAllTexts()
        call.respond(texts)
    }

    put("/texts") {
        val message = call.receiveText()
        textService.createText(message)
        call.respond("Text created")
    }
}