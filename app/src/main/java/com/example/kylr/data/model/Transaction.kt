package com.example.kylr.data.model

import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val fromVpa: String,
    val toVpa: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: TransactionStatus = TransactionStatus.PENDING,
    val note: String? = null,
    val type: TransactionType
)

enum class TransactionStatus {
    PENDING, SUCCESS, FAILED
}

enum class TransactionType {
    SENT, RECEIVED
}