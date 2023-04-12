package com.example.analyzerdrammamd

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.analyzerdrammamd.databinding.BankMaketBinding
import com.squareup.picasso.Picasso

class BankAdapter : RecyclerView.Adapter<BankAdapter.BankHolder>() {
    private lateinit var context :Context
    private var banksList = ArrayList<Bank>()

    class BankHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = BankMaketBinding.bind(view)
        fun bind(bank: Bank) = with(binding){
            Picasso.with(logoBank.context).load(bank.logoImageURl).into(logoBank)
            nameBank.text = bank.name
            coutseBank.text = bank.course.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bank_maket, parent, false)
        context = parent.context
        return BankHolder(view)
    }

    override fun getItemCount(): Int {
        return banksList.size
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        holder.bind(banksList[position])
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addBank(bank: Bank){
        banksList.add(bank)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun test(arrayList: ArrayList<Bank>){
        this.banksList = arrayList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearBuildAdapterList() {
        this.banksList.clear()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeRVItem(bank: Bank){
        banksList.remove(bank)
        notifyDataSetChanged()
    }


}