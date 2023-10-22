package com.example.a202310212
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


data class perDayEntry(var day: Int, var item: String, var completed: Boolean)
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
        addData(prefs!!,"item1",2,false)
        addData(prefs!!,"item3",2,false)
        setContent {
            Viewtasks(prefs!!, 2)
            }
        }
    }

@Composable
fun Viewtasks(prefs: Prefs, day: Int) {
    var data = readData(prefs)
    Log.d("another test", data.toString())
    LazyColumn () {
        for (i in data.indices) if (data[i].day == day) {
            item{
                Row{
                    var checked by remember {
                        mutableStateOf (false)
                    }
                    var removed by remember {
                        mutableStateOf (false)
                    }
                    Checkbox (
                        checked = checked,
                        modifier = Modifier.padding(16.dp),
                        onCheckedChange = {
                            checked_ -> checked = checked_
                            data[i].completed = checked
                            prefs.mainData = JSON.writeValueAsString(data)
                            Log.d("CHECKTEST", data[i].toString())
                        }
                    )
                    Text(text = data[i].item,
                        modifier = Modifier.padding(16.dp), fontSize = 30.sp)
                    Checkbox(
                        checked = removed,
                        modifier = Modifier.padding(16.dp),
                        onCheckedChange = fun(removed_: Boolean) {
                            removed = removed_
                            if (removed) {
                                data.remove(data[i])
                                removeData(prefs, i)
                                // Viewtasks(prefs = prefs, day = day)
                            }
                        }
                    )

                }
            }
        }
    }
}

fun readData(prefs: Prefs): MutableList<perDayEntry> {
    return JSON.readValue(prefs.mainData!!)
}

fun removeData(prefs: Prefs, idx: Int) {
    val entries = readData(prefs)
    entries.remove(entries[idx])
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun addData(prefs: Prefs, item: String, day: Int, completed: Boolean) {
    val entries = readData(prefs)
    entries.add(perDayEntry(day,item, completed))
    prefs.mainData = JSON.writeValueAsString(entries)

}
