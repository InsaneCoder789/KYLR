package com.example.kylr.ui.bank

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kylr.data.model.Bank
import com.example.kylr.databinding.ActivityBankSelectionBinding
import com.example.kylr.ui.upi.UpiSetupActivity

class BankSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBankSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val mockBanks = listOf(
            Bank("1", "State Bank of India", "", "SBIN"),
            Bank("2", "HDFC Bank", "", "HDFC"),
            Bank("3", "ICICI Bank", "", "ICIC"),
            Bank("4", "Axis Bank", "", "UTIB"),
            Bank("5", "Punjab National Bank", "", "PUNB"),
            Bank("6", "Bank of Baroda", "", "BARB")
        )

        val adapter = BankAdapter(mockBanks) { bank ->
            onBankSelected(bank)
        }

        binding.rvBanks.layoutManager = LinearLayoutManager(this)
        binding.rvBanks.adapter = adapter
    }

    private fun onBankSelected(bank: Bank) {
        Toast.makeText(this, "Fetching accounts for ${bank.name}...", Toast.LENGTH_SHORT).show()
        
        // Simulating backend call to NPCI to fetch accounts linked with this mobile number
        binding.root.postDelayed({
            val intent = Intent(this, UpiSetupActivity::class.java)
            intent.putExtra("BANK_NAME", bank.name)
            intent.putExtra("BANK_ID", bank.id)
            startActivity(intent)
        }, 1500)
    }
}