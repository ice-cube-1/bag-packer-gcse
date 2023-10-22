package com.example.a202310212
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

val prefs: Prefs by lazy {
    MainActivity.prefs!!
}

data class perDayEntry(var day: Int, var item: String)
val JSON = jacksonObjectMapper()

class MainActivity: ComponentActivity()
{
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Prefs(applicationContext)
        instance = this
        val dataCurrent = listOf(perDayEntry(1,"Test1"), perDayEntry(2,"test2"))
        val jsontest = JSON.writeValueAsString(dataCurrent)
        prefs!!.mainData = jsontest
        val fromjsonpref = prefs!!.mainData
        val backToList: List<perDayEntry> = JSON.readValue(fromjsonpref!!)
        Log.d("TEST2", backToList.toString())
        addData(prefs!!, "Hello",0)
        prefs!!.mainData?.let { Log.d("TEST", it) }
    }
}


fun addData(prefs: Prefs, item: String, day: Int) {
    val pref = prefs.mainData
    val prefnew = pref.plus(".-.").plus(day.toString()).plus(".").plus(item)
    prefs.mainData = prefnew
}
