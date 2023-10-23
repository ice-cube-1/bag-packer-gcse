package com.example.a202310212
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.Calendar

var currentday = Math.floorMod((Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2),7)

data class perDayEntry(var day: Int, var item: String, var completed: Boolean)
val JSON = jacksonObjectMapper()
val daysOfWeek = listOf<String>("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")


class MainActivity: ComponentActivity()
{
    companion object {
        var prefs: Prefs? = null
        lateinit var instance: MainActivity
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("initday", currentday.toString())
        super.onCreate(savedInstanceState)
        prefs = Prefs(applicationContext)
        instance = this
        //addData(prefs!!,"item1",2,false)
        //addData(prefs!!,"item3",2,false)
        setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "homeScreen" ) {
                    composable("homeScreen") {
                        homeScreen(prefs = prefs!!, navigation = navController)
                    }
                    composable("displayTasks/{dayToDisplay}") { backStackEntry ->
                        val dayToDisplay = backStackEntry.arguments?.getString("dayToDisplay")
                        Log.d("stuffithink", dayToDisplay.toString())
                        val newday = dayToDisplay!!.toInt()
                        displayTasks(prefs = prefs!!, navigation = navController, day = newday)
                    }
                    composable("addTask") {
                        addTask(prefs = prefs!!, navigation = navController)
                    }
                }
            }
            }
        }

@Composable
fun homeScreen(modifier: Modifier = Modifier, prefs: Prefs, navigation: NavController) {
    var goaddtask by remember { mutableStateOf(false) }
    var goviewlist by remember { mutableStateOf(-1) }
    confirmUncheck(prefs, Math.floorMod(currentday-1,7))
    if (goviewlist != -1) {
        LaunchedEffect(Unit) {
            navigation.navigate("displayTasks/{daytogo}".replace(oldValue = "{daytogo}", newValue = goviewlist.toString()))
        }    }
    if (goaddtask) {
        LaunchedEffect(Unit) {
            navigation.navigate("addTask")
        }    }
    Column(modifier = Modifier.padding(16.dp)) {
        addtaskButton(
            AddTask = {
                goaddtask = true
            }
        )
        for (i in 0..6) {
            Button(onClick = { goviewlist = i }) {
                Text(daysOfWeek[i])
            }
        }
    }
}

@Composable
fun addtaskButton(AddTask: () -> Unit) {
    Column {
        Button(onClick = AddTask) {
            Text("Add Task")
        }
    }
}

@Composable
fun addTaskOptions(
    taskValue: String,
    taskName: (String) -> Unit,
    submit: () -> Unit
) {
    Column (modifier = Modifier.padding(16.dp)) {
        TextField(
            value = taskValue,
            onValueChange = taskName
        )
        Button(onClick = submit) {
            Text("Submit")
        }
    }
}
@Composable
fun addTask(prefs: Prefs, navigation: NavController,modifier: Modifier = Modifier) {
    var goHome by remember { mutableStateOf(false) }
    var taskValue by remember { mutableStateOf("") }
    var checkedState by remember { mutableStateOf(mutableListOf<Boolean>(false,false,false,false,false,false,false))}
    if (goHome) {
        if (taskValue != "") {
            for (i in 0..6) {
                if (checkedState[i]) {
                    addData(prefs,taskValue,i,false)
                }
            }
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        for (i in 0..6) {
            var individualCheck by remember { mutableStateOf(false) }
            Row {
                Checkbox(checked = individualCheck, onCheckedChange = {
                    individualCheck = it
                    checkedState[i] = it
                })
                Text(text = daysOfWeek[i], modifier = Modifier.padding(16.dp))
            }
        }

        addTaskOptions(taskValue = taskValue, taskName = {
            taskValue = it
        }, submit = {
            goHome = true
        }
        )
    }
}

@Composable
fun TaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = taskName,
            modifier = Modifier.padding(16.dp)
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }

    }
}

@Composable
fun backButton(
    onPress: () -> Unit
) {
    Button(onClick = onPress) {
        Text("Go Back")
    }
}

@Composable
fun displayTasks(prefs: Prefs, modifier: Modifier = Modifier, navigation: NavController, day: Int) {
    var goHome by remember { mutableStateOf(false) }
    var removable by remember { mutableStateOf(mutableListOf<perDayEntry>()) }
    var tasks = readDataForDay(prefs,day)
    if (goHome) {
        for (i in removable) {
            removeData(prefs, i.day, i.completed,i.item)
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        Text(text= daysOfWeek[day])
        for (i in tasks) {
            var showTask by remember { mutableStateOf(true) }
            var checked by remember { mutableStateOf(i.completed)}
            if (showTask) {
                TaskItem(taskName = i.item, checked = checked,
                    onCheckedChange = {
                        checked = it
                        changeCompletion(prefs,i)
                        i.completed = it
                    },
                    onClose = {
                    showTask = false
                        removable.add(i)
                })
            }
        }
        backButton(onPress = {
            goHome = true
        })
    } }

fun confirmUncheck(prefs: Prefs, day: Int) {
    var entries = readData(prefs)
    for (i in 0..entries.size-1) {
        if (entries[i].day == day) {
            entries[i].completed=false
        }
    }
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun readData(prefs: Prefs): MutableList<perDayEntry> {
    return JSON.readValue(prefs.mainData!!)
}

fun changeCompletion(prefs: Prefs,toChange: perDayEntry) {
    var entries = readData(prefs)
    entries[entries.indexOf(toChange)].completed = !entries[entries.indexOf(toChange)].completed
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun removeData(prefs: Prefs, day: Int, completed: Boolean, item: String) {
    val entries = readData(prefs)
    entries.remove(perDayEntry(day,item,completed))
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun editRecentItem(prefs: Prefs, newItem: String) {
    var entries = readData(prefs)
    entries[entries.indexOf(entries.last())].item = newItem
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun addData(prefs: Prefs, item: String, day: Int, completed: Boolean) {
    var entries = readData(prefs)
    entries.add(perDayEntry(day,item, completed))
    entries = entries.distinct().toMutableList()
    prefs.mainData = JSON.writeValueAsString(entries)

}

fun readDataForDay(prefs: Prefs, day: Int): MutableList<perDayEntry> {
    var entries = readData(prefs)
    var subsetEntries = mutableListOf<perDayEntry>()
    for (i in entries) {
        if (i.day == day) {
            subsetEntries.add(i)
        }
    }
    return subsetEntries
}
