package com.example.kylr.data.api

import com.example.kylr.data.model.Bank
import com.example.kylr.data.model.BankAccount
import retrofit2.Response
import retrofit2.http.*

interface UpiApi {

    @POST("v1/device/bind")
    suspend fun initiateDeviceBinding(@Body request: DeviceBindingRequest): Response<BindingResponse>

    @GET("v1/banks")
    suspend fun getSupportedBanks(): Response<List<Bank>>

    @GET("v1/accounts/link")
    suspend fun fetchLinkedAccounts(@Query("bankId") bankId: String): Response<List<BankAccount>>

    @POST("v1/vpa/create")
    suspend fun createVpa(@Body request: VpaRequest): Response<VpaResponse>

    @POST("v1/payments/execute")
    suspend fun executePayment(@Body request: ExecutePaymentRequest): Response<ExecutePaymentEnvelope>
}

data class DeviceBindingRequest(val mobile: String, val deviceId: String, val smsToken: String)
data class BindingResponse(val status: String, val message: String)
data class VpaRequest(val accountId: String, val suggestedVpa: String)
data class VpaResponse(val status: String, val vpa: String)

data class ExecutePaymentRequest(
    val txId: String,
    val idempotencyKey: String,
    val senderWalletId: String,
    val receiverWalletId: String,
    val amountMinor: Long,
    val currency: String,
    val channel: String,
    val offlineTokenId: String?,
    val createdAt: String,
)

data class ExecutePaymentEnvelope(
    val result: PipelineResultDto?,
)

data class PipelineResultDto(
    val status: String,
    val ledgerEntryId: String?,
    val reason: String?,
)