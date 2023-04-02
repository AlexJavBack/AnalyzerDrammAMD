package com.example.analyzerdrammamd

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.analyzerdrammamd.databinding.BankMaketBinding
import com.squareup.picasso.Picasso

class BankAdapter : RecyclerView.Adapter<BankAdapter.BankHolder>() {
    lateinit var context :Context
    val banksList = ArrayList<Bank>()

    class BankHolder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = BankMaketBinding.bind(view)
        fun bind(bank: Bank) = with(binding){
            Picasso.with(logoBank.context).load(bank.logoImageURl).into(logoBank)
            nameBank.text = bank.name
            coutseBank.text = bank.course.toString()
            Log.d("Log", "шаг адаптер 1")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bank_maket, parent, false)
        Log.d("Log", "шаг адаптер 2")
        context = parent.context
        return BankHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("Log", "шаг адаптер 3")
        return banksList.size
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        Log.d("Log", "шаг адаптер 4")
        holder.bind(banksList[position])
    }
    fun addBank(bank: Bank){
        banksList.add(bank)
        notifyDataSetChanged()
    }


}