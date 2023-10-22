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
        addData(prefs!!,"newitem",2)
        Log.d("test3",readData(prefs!!).toString())
        removeData(prefs!!,"hello",1)
        Log.d("test3",readData(prefs!!).toString())
    }
}

fun readData(prefs: Prefs): MutableList<perDayEntry> {
    return JSON.readValue(prefs.mainData!!)
}

fun removeData(prefs: Prefs, item: String, day:Int) {
    var entries = readData(prefs)
    entries.remove(perDayEntry(day,item))
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun addData(prefs: Prefs, item: String, day: Int) {
    var entries = readData(prefs)
    entries.add(perDayEntry(day,item))
    prefs.mainData = JSON.writeValueAsString(entries)

}
