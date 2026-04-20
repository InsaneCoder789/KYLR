package com.example.kylr.ui.bank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kylr.data.model.Bank
import com.example.kylr.databinding.ItemBankBinding

class BankAdapter(
    private val banks: List<Bank>,
    private val onBankSelected: (Bank) -> Unit
) : RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    class BankViewHolder(val binding: ItemBankBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val binding = ItemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        val bank = banks[position]
        holder.binding.tvBankName.text = bank.name
        holder.binding.root.setOnClickListener { onBankSelected(bank) }
    }

    override fun getItemCount() = banks.size
}