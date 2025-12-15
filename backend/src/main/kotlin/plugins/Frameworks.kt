package com.int531.plugins

import com.int531.repositories.TextRepository
import com.int531.services.TextService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

private val appModule = module {
    single { TextRepository() }
    single { TextService(get()) }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
