package com.example.kylr.data.model

data class Bank(
    val id: String,
    val name: String,
    val logoUrl: String,
    val iifscPrefix: String
)

data class BankAccount(
    val accountId: String,
    val bankName: String,
    val maskedAccountNumber: String,
    val accountType: String,
    val ifsc: String,
    var vpa: String? = null
)