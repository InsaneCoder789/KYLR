package com.example.kylr.ui.upi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kylr.MainActivity
import com.example.kylr.databinding.ActivityUpiSetupBinding

class UpiSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpiSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpiSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bankName = intent.getStringExtra("BANK_NAME") ?: "your bank"
        Toast.makeText(this, "Linking accounts from $bankName", Toast.LENGTH_SHORT).show()

        binding.btnCreateVpa.setOnClickListener {
            val vpa = binding.etVpa.text.toString()
            if (vpa.isNotEmpty()) {
                // In a real app, you would call the backend to register the VPA
                // through the PSP (Payment Service Provider) which connects to NPCI.
                completeSetup(vpa)
            } else {
                Toast.makeText(this, "Please enter a UPI ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun completeSetup(vpa: String) {
        val prefs = getSharedPreferences("KYLR_PREFS", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("is_onboarded", true)
            putString("user_vpa", "$vpa@kylr")
            apply()
        }

        Toast.makeText(this, "UPI ID $vpa@kylr created successfully!", Toast.LENGTH_LONG).show()
        
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}