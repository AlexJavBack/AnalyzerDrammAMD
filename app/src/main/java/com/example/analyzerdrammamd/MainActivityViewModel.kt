package com.example.analyzerdrammamd

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel : ViewModel() {
    var banksList : MutableLiveData<ArrayList<Bank>> = MutableLiveData()
    var banksListSale : MutableLiveData<ArrayList<Bank>> = MutableLiveData()
    var tittleCurrency = MutableLiveData<String>()
    var mainImage = MutableLiveData<Drawable>()
    var buttonImagesOne = MutableLiveData<Drawable>()
    var buttonImagesTwo = MutableLiveData<Drawable>()
    var buttonImagesThree = MutableLiveData<Drawable>()
    private var thread: Thread
    private var runnable: Runnable
    lateinit var document : Document
    var bool = false


    init {
        runnable = Runnable {
            getWeb()
        }
        thread = Thread(runnable)
        thread.start()
        thread.join()
        thread.interrupt()
        Log.d("Log", "шаг v 1")
    }

    fun saveList(array: ArrayList<Bank>, arrayTwo: ArrayList<Bank> ){
        banksList.value = array
        banksListSale.value = arrayTwo
    }

    fun saveText(text : String){
        tittleCurrency.value = text
    }

    fun saveImage(drawable: Drawable){
        mainImage.value = drawable
    }
    fun saveImageBOne(drawable: Drawable){
        buttonImagesOne.value = drawable
    }
    fun saveImageBTwo(drawable: Drawable){
        buttonImagesTwo.value = drawable
    }
    fun saveImageBThree(drawable: Drawable){
        buttonImagesThree.value = drawable
    }

    private fun getWeb() : Document{
            Log.d("Log", "шаг v 2")
            for (language in Languages.values()) {
                when (Locale.getDefault().language.uppercase()) {
                    "RU" -> {
                        document = Jsoup.connect("https://rate.am/ru/").get()
                        return document
                    }
                    "EN" -> {
                        document = Jsoup.connect("https://rate.am/en/").get()
                        return document
                    }
                    "AM" -> {
                        document = Jsoup.connect("https://rate.am/am/").get()
                        return document
                    }
                }
                if (language.toString() != Locale.getDefault().language) {
                    document = Jsoup.connect("https://rate.am/en/").get()
                    return document
                }
            }
        return document
    }



}