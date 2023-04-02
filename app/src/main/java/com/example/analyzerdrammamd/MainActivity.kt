package com.example.analyzerdrammamd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.analyzerdrammamd.databinding.ActivityMainBinding
import com.example.analyzerdrammamd.databinding.BankMaketBinding
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    private val bankAdapter = BankAdapter()
    private val bankAdaptertwo = BankAdapter()
    private val imageIdList = listOf(R.drawable.plant1)
    private val banksList = ArrayList<Bank>()
    private val banksListSale = ArrayList<Bank>()
    private var appAlreadyBuilt = false
    private var currentСurrency = "USD"
    private val currencyIndex = arrayOf(intArrayOf(5,6), intArrayOf(7,8), intArrayOf(9,10), intArrayOf(11,12))
    private var index = 0
    lateinit var view: View
    lateinit var document : Document
    lateinit var thread: Thread
    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()
        Log.d("Log", "шаг 3")
        for(bank in getMaxCourse(banksList)){
            bankAdapter.addBank(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdaptertwo.addBank(bank)
        }
        //var elementsss : Elements = banksNumList.
        //mainBinding.textView.text = banksNumList[1].toString()
    }

    override fun onResume() {
        super.onResume()
        appAlreadyBuilt = true
    }

    private fun init(){
        mainBinding.apply {
            rcVBanksPurchase.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcVBanksSale.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcVBanksPurchase.adapter =  bankAdapter
            rcVBanksSale.adapter = bankAdaptertwo
            runnable = Runnable {
                getWeb()
            }
            thread = Thread(runnable)
            thread.start()
            thread.join()
            Log.d("Log", "шаг 1")
        }
        Log.d("Log", "шаг 10")

    }

    private fun getWeb(){
            document = Jsoup.connect("https://rate.am/ru/").get()
            var table : Elements = document.getElementsByTag("tbody")
            var ourTable : Element? = table.get(3)
            for(index in 2 until  ourTable!!.childrenSize() - 5){
                val e : Elements = ourTable.children().get(index).child(1).getElementsByTag("img")
                val bankLogoUrl = e.attr("src")
                if (ourTable.children().get(index).child(5).text().toString().isEmpty()){
                    break
                }
                val bank = Bank(bankLogoUrl, ourTable.children().get(index).child(1).text(), ourTable.children().get(index).child(5).text().toString().toDouble())
                val bankSale = Bank(bankLogoUrl, ourTable.children().get(index).child(1).text(), ourTable.children().get(index).child(6).text().toString().toDouble())
                banksList.add(bank)
                banksListSale.add(bankSale)
            }
        }
    fun onClickBOne(view : View){
        imageButtonDetected(view)
        getDataFromWebsite(document)
        for(bank in getMaxCourse(banksList)){
            bankAdapter.addBank(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdaptertwo.addBank(bank)
        }
    }
    fun getMaxCourse(array : ArrayList<Bank>) : ArrayList<Bank> {
        var max = 0.0
        var arrayResult = arrayListOf<Bank>()
        for(i in array.indices){
            if(array[i].course > max){
                max = array[i].course
            }
        }
        for(i in array.indices){
            if(array[i].course >= max){
                arrayResult.add(array[i])
            }
        }
        return arrayResult
    }
    fun getDataFromWebsite(document: Document){
        if(mainBinding.textView.text == currentСurrency){
            var table : Elements = document.getElementsByTag("tbody")
            var ourTable : Element? = table.get(3)
            when(view.contentDescription){
                getString(R.string.euro_desc) -> {
                    removeArrays(banksList,banksListSale)
                    currentСurrency = getString(R.string.euro_cod)
                    mainBinding.textView.text = currentСurrency
                    for(index in 2 until  ourTable!!.childrenSize() - 5){
                        val e : Elements = ourTable.children().get(index).child(1).getElementsByTag("img")
                        val bankLogoUrl = e.attr("src")
                        if (ourTable.children().get(index).child(7).text().toString().isEmpty()){
                            break
                        }
                        val bank = Bank(bankLogoUrl, ourTable.children().get(index).child(1).text(), ourTable.children().get(index).child(currencyIndex[getIndexElement(currentСurrency)!!][0]).text().toString().toDouble())
                        val bankSale = Bank(bankLogoUrl, ourTable.children().get(index).child(1).text(), ourTable.children().get(index).child(currencyIndex[getIndexElement(currentСurrency)!!][1]).text().toString().toDouble())
                        banksList.add(bank)
                        banksListSale.add(bankSale)
                    }
                }
            }
        }
        else {
            for(bank in getMaxCourse(banksList)){
                bankAdapter.addBank(bank)
            }
            for(bank in getMaxCourse(banksListSale)){
                bankAdaptertwo.addBank(bank)
            }
        }

    }
    fun removeArrays(arrayPurchase: ArrayList<Bank>, arraySale: ArrayList<Bank>) {
        arrayPurchase.removeAll(arrayPurchase)
        arraySale.removeAll(arraySale)
    }
    fun imageButtonDetected(view: View){
        this.view = view
    }
    fun getIndexElement(codValute : String) : Int? {
        when(codValute){
            "USD" -> return 0
            "EUR" -> return 1
            "RUR" -> return 2
            "GBP" -> return 3
        }
        return null
    }

}
