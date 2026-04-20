package com.example.kylr.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kylr.databinding.ActivityOnboardingBinding
import com.example.kylr.ui.bank.BankSelectionActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            val mobile = binding.etMobileNumber.text.toString()
            if (mobile.length == 10) {
                // In a real app, this would trigger SMS-based Device Binding
                // As per NPCI, we must send an SMS from the user's phone to our server
                startDeviceBinding(mobile)
            } else {
                Toast.makeText(this, "Enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startDeviceBinding(mobile: String) {
        // Mocking the binding process
        Toast.makeText(this, "Binding Device via SMS...", Toast.LENGTH_LONG).show()
        
        // After successful binding and OTP verification
        val intent = Intent(this, BankSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }
}