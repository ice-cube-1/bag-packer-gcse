package com.example.a202310212
import android.os.Bundle
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
        //addData(prefs!!,"item1",2,false)
        //addData(prefs!!,"item3",2,false)
        setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "homeScreen" ) {
                    composable("homeScreen") {
                        homeScreen(prefs = prefs!!, navigation = navController)
                    }
                    composable("displayTasks") {
                        displayTasks(prefs = prefs!!, navigation = navController)
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
    var goviewlist by remember { mutableStateOf(false) }
    if (goviewlist) {
        navigation.navigate("displayTasks")
    }
    if (goaddtask) {
        navigation.navigate("addTask")
    }
    Column(modifier = Modifier.padding(16.dp)) {
        homeOptions(
            AddTask = {
                addData(prefs,"",1,false)
                goaddtask = true
            },
            ViewList = {
                goviewlist = true
            }
        )
    }
}

@Composable
fun homeOptions(AddTask: () -> Unit, ViewList: () -> Unit) {
    Column {
        Button(onClick = AddTask) {
            Text("Add Task")
        }
        Button(onClick = ViewList) {
            Text("View list for [a day]")
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
    if (goHome) {
        navigation.navigate("homeScreen")
    }
    addTaskOptions(taskValue = taskValue, taskName = {
        taskValue = it
        editRecentItem(prefs,taskValue)
    }, submit = {
        goHome = true
    }
    )
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
fun displayTasks(prefs: Prefs, modifier: Modifier = Modifier, navigation: NavController) {
    var goHome by remember { mutableStateOf(false) }
    var tasks = readData(prefs)
    if (goHome) {
        navigation.navigate("homeScreen")
    }
    Column(modifier = modifier.padding(16.dp)) {
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
                    removeData(prefs,i.day,i.completed,i.item)
                })
            }
        }
        backButton(onPress = {
            goHome = true
        })
    }
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
    val entries = readData(prefs)
    entries.add(perDayEntry(day,item, completed))
    prefs.mainData = JSON.writeValueAsString(entries)

}
