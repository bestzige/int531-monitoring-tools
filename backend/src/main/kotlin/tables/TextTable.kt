package com.int531.tables

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable

object TextTable : UUIDTable() {
    val message = text("message")
}

@Serializable
data class TextRecord(
    val id: String,
    val message: String
)
