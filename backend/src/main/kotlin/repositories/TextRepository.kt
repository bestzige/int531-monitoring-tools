package com.int531.repositories

import com.int531.tables.TextRecord
import com.int531.tables.TextTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TextRepository {
    init {
        transaction {
            SchemaUtils.create(TextTable)
        }
    }

    suspend fun getAllTexts(): List<TextRecord> = withContext(Dispatchers.IO) {
        transaction {
            TextTable.selectAll()
                .toList()
                .map { it.toTextRecord() }
        }
    }

    suspend fun create(newMessage: String) = withContext(Dispatchers.IO) {
        transaction {
            TextTable.insert {
                it[message] = newMessage
            }
        }
    }

    private fun ResultRow.toTextRecord(): TextRecord {
        return TextRecord(
            id = this[TextTable.id].value.toString(),
            message = this[TextTable.message]
        )
    }
}