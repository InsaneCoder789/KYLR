package com.example.kylr.data.payment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object UpiIntentLauncher {
    /**
     * Hands off to an installed UPI PSP app (PhonePe, GPay, etc.). Money movement happens inside that app
     * on NPCI-regulated rails; KYLR does not settle funds directly from this intent.
     */
    fun launchPay(
        context: Context,
        payeeVpa: String,
        payeeName: String,
        amountInInr: String?,
        transactionNote: String?,
    ) {
        val builder = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", payeeVpa)
            .appendQueryParameter("pn", payeeName)
            .appendQueryParameter("cu", "INR")

        if (!amountInInr.isNullOrBlank()) {
            builder.appendQueryParameter("am", amountInInr)
        }
        if (!transactionNote.isNullOrBlank()) {
            builder.appendQueryParameter("tn", transactionNote)
        }

        val intent = Intent(Intent.ACTION_VIEW, builder.build())
        val chooser = Intent.createChooser(intent, "Complete payment in UPI app")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(chooser)
        } catch (_: Exception) {
            Toast.makeText(context, "No UPI app available to handle this payment", Toast.LENGTH_LONG).show()
        }
    }
}
