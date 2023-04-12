package com.example.analyzerdrammamd

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.analyzerdrammamd.databinding.ActivityMainBinding
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val bankAdapter = BankAdapter()
    private val bankAdapterTwo = BankAdapter()
    private val banksList = ArrayList<Bank>()
    private val banksListSale = ArrayList<Bank>()
    private var currentCurrency = "Dollar"
    private val currencyIndex = arrayOf(intArrayOf(5,6), intArrayOf(7,8), intArrayOf(9,10), intArrayOf(11,12))
    private lateinit var view: View
    private lateinit var document : Document
    private val mViewModel : MainActivityViewModel by lazy { ViewModelProvider(this)[MainActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.banksList.observe(this) {
            it?.let {
                bankAdapter.test(it)
            }
        }
        mViewModel.banksListSale.observe(this) {
            it?.let {
                bankAdapterTwo.test(it)
            }
        }
        mViewModel.tittleCurrency.observe(this) {
            mainBinding.textView.text = it
        }
        mViewModel.mainImage.observe(
            this,
        ) {
            mainBinding.imageViewCurrentCurrency.setImageDrawable(it)
        }
        mViewModel.buttonImagesOne.observe(this) {
            mainBinding.imageButton1.setImageDrawable(it)
        }
        mViewModel.buttonImagesTwo.observe(this) {
            mainBinding.imageButton2.setImageDrawable(it)
        }
        mViewModel.buttonImagesThree.observe(this) {
            mainBinding.imageButton3.setImageDrawable(it)
        }
    }

    private fun init(){
        mainBinding.apply {
            getWeb()
            rcVBanksPurchase.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcVBanksSale.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcVBanksPurchase.adapter =  bankAdapter
            rcVBanksSale.adapter = bankAdapterTwo
        }

    }

    private fun getWeb(){
            document = mViewModel.document
            if(!mViewModel.bool) {
                val table: Elements = document.getElementsByTag("tbody")
                val ourTable: Element? = table[3]
                for (index in 2 until ourTable!!.childrenSize() - 5) {
                    val e: Elements = ourTable.children()[index].child(1).getElementsByTag("img")
                    val bankLogoUrl = e.attr("src")
                    if (ourTable.children()[index]
                            .child(currencyIndex[getIndexElement(currentCurrency)!!][0]).text()
                            .toString().isEmpty() || ourTable.children()[index]
                            .child(currencyIndex[getIndexElement(currentCurrency)!!][1]).text()
                            .toString().isEmpty()
                    ) {
                        continue
                    }
                    val bank = Bank(
                        bankLogoUrl,
                        ourTable.children()[index].child(1).text(),
                        ourTable.children()[index].child(5).text().toString().toDouble()
                    )
                    val bankSale = Bank(
                        bankLogoUrl,
                        ourTable.children()[index].child(1).text(),
                        ourTable.children()[index].child(6).text().toString().toDouble()
                    )
                    banksList.add(bank)
                    banksListSale.add(bankSale)
                    Log.d("Log", "шаг 55000")
                }
                Log.d("Log", "шаг 33")
                mViewModel.bool = true
                mViewModel.saveList(getMaxCourse(banksList), getMaxCourse(banksListSale))
                mViewModel.saveText(mainBinding.textView.text.toString())
                mViewModel.saveImage(mainBinding.imageViewCurrentCurrency.drawable)
            }
        }
    fun onClickBOne(view : View){
        imageButtonDetected(view)
        getDataFromWebsite(document)
        for(bank in getMaxCourse(banksList)){
            bankAdapter.addBank(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdapterTwo.addBank(bank)
        }
    }
    fun onClickBTwo(view : View){
        imageButtonDetected(view)
        getDataFromWebsite(document)
        for(bank in getMaxCourse(banksList)){
            bankAdapter.addBank(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdapterTwo.addBank(bank)
        }
    }
    fun onClickBThree(view : View){
        imageButtonDetected(view)
        getDataFromWebsite(document)
        for(bank in getMaxCourse(banksList)){
            bankAdapter.addBank(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdapterTwo.addBank(bank)
        }
    }
    private fun getMaxCourse(array : ArrayList<Bank>) : ArrayList<Bank> {
        var max = 0.0
        val arrayResult = arrayListOf<Bank>()
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
    private fun getDataFromWebsite(document: Document){
        if(view.contentDescription != mainBinding.imageViewCurrentCurrency.contentDescription){
            removeDataFromAdapters()
            removeArrays(banksList,banksListSale)
            val table : Elements = document.getElementsByTag("tbody")
            val ourTable : Element? = table[3]
            when(view.contentDescription){
                getString(R.string.euro_desc) -> {
                    currentCurrency = getString(R.string.euro_desc)
                    mainBinding.textView.text = getString(R.string.euro_cod)
                    mViewModel.saveText(mainBinding.textView.text.toString())
                    getImageForButton(view.id)
                    mainBinding.imageViewCurrentCurrency.contentDescription = currentCurrency
                    mainBinding.imageViewCurrentCurrency.setImageResource(R.drawable.euro)
                    mViewModel.saveImage(mainBinding.imageViewCurrentCurrency.drawable)
                    ourTable?.let { addDataForList(it) }
                }
                getString(R.string.dollar_desc) -> {
                    currentCurrency = getString(R.string.dollar_desc)
                    mainBinding.textView.text = getString(R.string.dollar_cod)
                    mViewModel.saveText(mainBinding.textView.text.toString())
                    getImageForButton(view.id)
                    mainBinding.imageViewCurrentCurrency.contentDescription = currentCurrency
                    mainBinding.imageViewCurrentCurrency.setImageResource(R.drawable.dollar)
                    mViewModel.saveImage(mainBinding.imageViewCurrentCurrency.drawable)
                    ourTable?.let { addDataForList(it) }
                }
                getString(R.string.rubol_desc) -> {
                    currentCurrency = getString(R.string.rubol_desc)
                    mainBinding.textView.text = getString(R.string.rubol_cod)
                    mViewModel.saveText(mainBinding.textView.text.toString())
                    getImageForButton(view.id)
                    mainBinding.imageViewCurrentCurrency.contentDescription = currentCurrency
                    mainBinding.imageViewCurrentCurrency.setImageResource(R.drawable.rubol)
                    mViewModel.saveImage(mainBinding.imageViewCurrentCurrency.drawable)
                    ourTable?.let { addDataForList(it) }
                }
                getString(R.string.lair_desc) -> {
                    currentCurrency = getString(R.string.lair_desc)
                    mainBinding.textView.text = getString(R.string.lair_cod)
                    mViewModel.saveText(mainBinding.textView.text.toString())
                    getImageForButton(view.id)
                    mainBinding.imageViewCurrentCurrency.contentDescription = currentCurrency
                    mainBinding.imageViewCurrentCurrency.setImageResource(R.drawable.lari)
                    mViewModel.saveImage(mainBinding.imageViewCurrentCurrency.drawable)
                    ourTable?.let { addDataForList(it) }
                }
            }
        }
        else {
            for(bank in getMaxCourse(banksList)){
                bankAdapter.addBank(bank)
            }
            for(bank in getMaxCourse(banksListSale)){
                bankAdapterTwo.addBank(bank)
            }
        }

    }
    private fun removeArrays(arrayPurchase: ArrayList<Bank>, arraySale: ArrayList<Bank>) {
        arrayPurchase.removeAll(arrayPurchase.toSet())
        arraySale.removeAll(arraySale.toSet())
    }
    private fun imageButtonDetected(view: View){
        this.view = view
    }
    private fun getIndexElement(descCurrency : String) : Int? {
        when(descCurrency){
            "Dollar" -> return 0
            "Euro" -> return 1
            "Rubol" -> return 2
            "Lari" -> return 3
        }
        return null
    }
    private fun getImageForButton(id : Int) = with(mainBinding){
        val drawable : Drawable = imageViewCurrentCurrency.drawable
        when(id){
            imageButton1.id -> {
                imageButton1.setImageDrawable(drawable)
                imageButton1.contentDescription =  imageViewCurrentCurrency.contentDescription
                mViewModel.saveImageBOne(imageButton1.drawable)
            }
            imageButton2.id -> {
                imageButton2.setImageDrawable(drawable)
                imageButton2.contentDescription =  imageViewCurrentCurrency.contentDescription
                mViewModel.saveImageBTwo(imageButton2.drawable)
            }
            imageButton3.id -> {
                imageButton3.setImageDrawable(drawable)
                imageButton3.contentDescription =  imageViewCurrentCurrency.contentDescription
                mViewModel.saveImageBThree(imageButton3.drawable)
            }
        }
    }

    private fun addDataForList(ourTable : Element){
        for(index in 2 until  ourTable.childrenSize() - 5){
            val e : Elements = ourTable.children()[index].child(1).getElementsByTag("img")
            val bankLogoUrl = e.attr("src")
            if (ourTable.children()[index].child(currencyIndex[getIndexElement(currentCurrency)!!][0]).text().toString().isEmpty() || ourTable.children()[index].child(currencyIndex[getIndexElement(currentCurrency)!!][1]).text().toString().isEmpty()){
                continue
            }
            val bank = Bank(bankLogoUrl, ourTable.children()[index].child(1).text(), ourTable.children()[index].child(currencyIndex[getIndexElement(currentCurrency)!!][0]).text().toString().toDouble())
            val bankSale = Bank(bankLogoUrl, ourTable.children()[index].child(1).text(), ourTable.children()[index].child(currencyIndex[getIndexElement(currentCurrency)!!][1]).text().toString().toDouble())
            banksList.add(bank)
            banksListSale.add(bankSale)
        }
    }
    private fun removeDataFromAdapters(){
        bankAdapter.clearBuildAdapterList()
        bankAdapterTwo.clearBuildAdapterList()
        for(bank in getMaxCourse(banksList)){
            bankAdapter.removeRVItem(bank)
        }
        for(bank in getMaxCourse(banksListSale)){
            bankAdapterTwo.removeRVItem(bank)
        }
    }




}
