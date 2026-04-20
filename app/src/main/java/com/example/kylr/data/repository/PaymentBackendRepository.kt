package com.example.kylr.data.repository

import com.example.kylr.BuildConfig
import com.example.kylr.data.api.ExecutePaymentEnvelope
import com.example.kylr.data.api.ExecutePaymentRequest
import com.example.kylr.data.api.RetrofitClient
import com.example.kylr.data.model.Transaction
import com.example.kylr.data.model.TransactionStatus
import com.example.kylr.data.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

/**
 * Calls your Node payment-pipeline service for orchestration, idempotency, and outbox events.
 * This is not a substitute for bank/NPCI certification; it complements UPI handoff flows.
 */
class PaymentBackendRepository {
    private val api by lazy { RetrofitClient.instance }

    suspend fun executeOnServer(request: ExecutePaymentRequest): Result<ExecutePaymentEnvelope> {
        if (BuildConfig.RAIL_API_BASE_URL.isBlank()) {
            return Result.failure(IllegalStateException("KYLR_API_BASE_URL is blank"))
        }
        return try {
            val resp = api.executePayment(request)
            if (resp.isSuccessful && resp.body() != null) {
                Result.success(resp.body()!!)
            } else {
                Result.failure(IllegalStateException("HTTP ${resp.code()}: ${resp.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun buildExecuteRequest(
        payeeVpa: String,
        amountInInr: Double,
        channel: String,
        offlineTokenId: String?,
    ): ExecutePaymentRequest {
        val amountMinor = (amountInInr * 100.0).toLong().coerceAtLeast(1)
        return ExecutePaymentRequest(
            txId = "txn_${UUID.randomUUID()}",
            idempotencyKey = "idem_${UUID.randomUUID()}",
            senderWalletId = "wallet_sender_device",
            receiverWalletId = payeeVpa,
            amountMinor = amountMinor,
            currency = "INR",
            channel = channel,
            offlineTokenId = offlineTokenId,
            createdAt = utcIsoNow(),
        )
    }

    private fun utcIsoNow(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return fmt.format(Date())
    }

    fun toUiTransaction(
        payeeVpa: String,
        amountInInr: Double,
        envelope: ExecutePaymentEnvelope,
    ): Transaction {
        val ok = envelope.result?.status == "accepted"
        return Transaction(
            fromVpa = "you",
            toVpa = payeeVpa,
            amount = amountInInr,
            status = if (ok) TransactionStatus.SUCCESS else TransactionStatus.FAILED,
            note = envelope.result?.ledgerEntryId,
            type = TransactionType.SENT,
        )
    }
}
