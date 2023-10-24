package com.example.a202310212
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a202310212.MainActivity.Companion.fusedLocationClient
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

data class perDayEntry(var day: Int, var item: String, var completed: Boolean, var recurring: Boolean)
val JSON = jacksonObjectMapper()
val daysOfWeek = listOf<String>("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")


class MainActivity: ComponentActivity()
{
    companion object {
        lateinit var fusedLocationClient: FusedLocationProviderClient
        var prefs: Prefs? = null
        lateinit var instance: MainActivity
            private set
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        prefs = Prefs(applicationContext)
        instance = this
        val currentday = Math.floorMod((Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2),7)
        if (prefs!!.currentDay != currentday) {
            wipeDay(prefs!!, prefs!!.currentDay)
            prefs!!.currentDay = currentday
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "testName"
            val descriptionText = "testDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("testChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this, "testChannel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Have you remembered everything?")
            .setContentText("You have not marked everything as packed.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                getLocation() { latitude, longitude ->
                    if ((kotlin.math.abs(prefs!!.latitude.toDouble() - latitude) > 0.0025) &&
                        (kotlin.math.abs(prefs!!.longitude.toDouble() - longitude) > 0.0025)
                        && (stillToDo(prefs!!, prefs!!.currentDay))) {
                        with(NotificationManagerCompat.from(this@MainActivity)) {
                            notify(0,builder.build())
                        }
                        prefs!!.remindedToday=true
                    }
                }
            }
        }, 0, 1000)
        setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "homeScreen" ) {
                    composable("homeScreen") {
                        homeScreen(prefs = prefs!!, navigation = navController)
                    }
                    composable("displayTasks/{dayToDisplay}") { backStackEntry ->
                        val dayToDisplay = backStackEntry.arguments?.getString("dayToDisplay")
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

@SuppressLint("MissingPermission")
fun getLocation(callback: (Double, Double) -> Unit) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val lat = location.latitude
            val long = location.longitude
            callback(lat, long)
        } else {
            callback(0.0, 0.0) // Handle the case when location data is null
        }
    }
}

@Composable
fun homeScreen(modifier: Modifier = Modifier, prefs: Prefs, navigation: NavController) {
    var goaddtask by remember { mutableStateOf(false) }
    var goviewlist by remember { mutableStateOf(-1) }
    if (goviewlist != -1) {
        LaunchedEffect(Unit) {
            navigation.navigate("displayTasks/{daytogo}".replace(oldValue = "{daytogo}", newValue = goviewlist.toString()))
        }    }
    if (goaddtask) {
        LaunchedEffect(Unit) {
            navigation.navigate("addTask")
        }    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lists", fontSize = 20.sp)
        for (i in 0..6) {
            ElevatedButton(onClick = { goviewlist = i }) {
                Text(daysOfWeek[i],modifier = Modifier.padding(4.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            addtaskButton(
                AddTask = {
                    goaddtask = true
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                getLocation() { latitude, longitude ->
                    prefs.latitude = latitude.toFloat()
                    prefs.longitude = longitude.toFloat()
                }
                Log.d(prefs.latitude.toString(), prefs.longitude.toString())
            }
            ) {
                Text("Set Home Location")
            }
        }
    }
}

@Composable
fun addtaskButton(AddTask: () -> Unit) {
        OutlinedIconButton(onClick = AddTask) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task",modifier = Modifier.padding(12.dp))
        }
    }


@Composable
fun addTaskOptions(
    taskValue: String,
    taskName: (String) -> Unit,
) {
    Column (modifier = Modifier.padding(16.dp)) {
        TextField(
            value = taskValue,
            onValueChange = taskName
        )
    }
}
@Composable
fun addTask(prefs: Prefs, navigation: NavController,modifier: Modifier = Modifier) {
    var goHome by remember { mutableStateOf(false) }
    var taskValue by remember { mutableStateOf("") }
    var recurringValue by remember { mutableStateOf(true) }
    var checkedState by remember { mutableStateOf(mutableListOf<Boolean>(false,false,false,false,false,false,false))}
    if (goHome) {
        if (taskValue != "") {
            for (i in 0..6) {
                if (checkedState[i]) {
                    addData(prefs,taskValue,i,false,recurringValue)
                }
            }
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add Task", fontSize = 20.sp,)
        addTaskOptions(taskValue = taskValue, taskName = {
            taskValue = it
        }
        )
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
        Row {
            Text("Recurring:", modifier = Modifier.padding(16.dp))
            Switch(checked = recurringValue, onCheckedChange = {recurringValue = it})
        }
        Button(onClick = {goHome = true}) {
            Text("Submit")
        }
    }
}

@Composable
fun TaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    recurring: Boolean
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
        if (recurring) {
            Icon(Icons.Filled.Refresh, contentDescription = "Recurring",modifier = Modifier.padding(12.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close",)
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
            removeData(prefs, i.day, i.completed,i.item, i.recurring)
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        Text(text= daysOfWeek[day], fontSize = 20.sp)
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
                },
                    recurring = i.recurring)
            }
        }
        backButton(onPress = {
            goHome = true
        })
    } }

fun wipeDay(prefs: Prefs, day: Int) {
    prefs.remindedToday = false
    var entries = readData(prefs)
    for (i in 0..entries.size-1) {
        if (entries[i].day == day) {
            entries[i].completed=false
        }
    }
    var newentries = mutableListOf<perDayEntry>()
    for (i in entries) {
        if ((i.day != day) or (i.recurring)) {
            newentries.add(i)
        }
    }
    prefs.mainData = JSON.writeValueAsString(newentries)
}

fun readData(prefs: Prefs): MutableList<perDayEntry> {
    return JSON.readValue(prefs.mainData!!)
}

fun changeCompletion(prefs: Prefs,toChange: perDayEntry) {
    var entries = readData(prefs)
    entries[entries.indexOf(toChange)].completed = !entries[entries.indexOf(toChange)].completed
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun removeData(prefs: Prefs, day: Int, completed: Boolean, item: String, recurring: Boolean) {
    val entries = readData(prefs)
    entries.remove(perDayEntry(day,item,completed,recurring))
    prefs.mainData = JSON.writeValueAsString(entries)
}

fun addData(prefs: Prefs, item: String, day: Int, completed: Boolean,recurring: Boolean) {
    var entries = readData(prefs)
    entries.add(perDayEntry(day,item, completed, recurring))
    Log.d("added",perDayEntry(day,item,completed,recurring).toString())
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

fun stillToDo(prefs: Prefs, day: Int): Boolean {
    var entries = readData(prefs)
    for (i in entries) {
        if ((i.day==day) && (!i.completed)) {
            return true
        }
    }
    return false
}