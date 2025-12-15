package com.int531.services

import com.int531.repositories.TextRepository

class TextService(
    private val repository: TextRepository
) {
    suspend fun getAllTexts() = repository.getAllTexts()

    suspend fun createText(message: String) {
        repository.create(message)
    }
}