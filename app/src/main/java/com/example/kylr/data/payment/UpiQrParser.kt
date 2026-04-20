package com.example.kylr.data.payment

import android.net.Uri

data class ParsedUpiPayee(
    val payeeAddress: String,
    val displayName: String,
)

object UpiQrParser {
    /**
     * Accepts raw QR contents such as `upi://pay?pa=...&pn=...` or strings containing that substring.
     */
    fun parse(raw: String): ParsedUpiPayee? {
        val trimmed = raw.trim()
        val candidate = if (trimmed.startsWith("upi://")) trimmed else extractUpiSubstring(trimmed) ?: return null
        val uri = Uri.parse(candidate)
        if (uri.scheme != "upi" || uri.host != "pay") return null
        val pa = uri.getQueryParameter("pa")?.trim().orEmpty()
        if (pa.isBlank()) return null
        val pn = uri.getQueryParameter("pn")?.trim().orEmpty().ifBlank { pa }
        return ParsedUpiPayee(payeeAddress = pa, displayName = pn)
    }

    private fun extractUpiSubstring(raw: String): String? {
        val idx = raw.indexOf("upi://pay")
        if (idx < 0) return null
        return raw.substring(idx)
    }
}
