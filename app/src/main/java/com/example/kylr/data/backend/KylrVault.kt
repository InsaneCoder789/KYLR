package com.example.kylr.data.backend

import com.example.kylr.data.model.Transaction
import com.example.kylr.data.model.TransactionStatus
import com.example.kylr.data.model.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

/**
 * KYLR Vault: The unique offline pipeline for transaction switching.
 * This acts as the proprietary core "switch" for the KYLR ecosystem.
 */
object KylrVault {
    
    // In-memory "Bank Accounts" for mock transactions
    private val balances = mutableMapOf(
        "alexrivera@kylr" to 4820.50,
        "sarah@kylr" to 1250.00,
        "marcus@kylr" to 300.00
    )

    private val _transactionHistory = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionHistory: StateFlow<List<Transaction>> = _transactionHistory

    /**
     * Simulates the KYLR Vault processing a transaction.
     */
    suspend fun processTransaction(
        fromVpa: String,
        toVpa: String,
        amount: Double,
        note: String? = null
    ): Transaction {
        // Simulating the secure vault switching latency
        delay(1500)

        val fromBalance = balances[fromVpa] ?: 0.0
        
        val transaction = if (fromBalance >= amount) {
            // Secure settlement
            balances[fromVpa] = fromBalance - amount
            balances[toVpa] = (balances[toVpa] ?: 0.0) + amount
            
            Transaction(
                fromVpa = fromVpa,
                toVpa = toVpa,
                amount = amount,
                note = note,
                status = TransactionStatus.SUCCESS,
                type = TransactionType.SENT
            )
        } else {
            Transaction(
                fromVpa = fromVpa,
                toVpa = toVpa,
                amount = amount,
                note = note,
                status = TransactionStatus.FAILED,
                type = TransactionType.SENT
            )
        }

        // Add to history
        val currentList = _transactionHistory.value.toMutableList()
        currentList.add(0, transaction)
        _transactionHistory.value = currentList
        
        return transaction
    }

    fun getBalance(vpa: String): Double {
        return balances[vpa] ?: 0.0
    }
}