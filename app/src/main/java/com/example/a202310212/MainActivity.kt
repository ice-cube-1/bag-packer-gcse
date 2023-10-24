package com.example.a202310212

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a202310212.MainActivity.Companion.fusedLocationClient
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

// class for each item that will be added to the list of items, which is then serialized into preferences
data class ItemToPack(
    var day: Int, var item: String, var completed: Boolean, var recurring: Boolean
)

// for serializing / deserializing in future
val JSON = jacksonObjectMapper()

// for the text that converts 0-6 into monday-sunday
val daysOfWeek =
    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


class MainActivity : ComponentActivity() {
    // initializes global variables for the location and preferences
    companion object {
        lateinit var fusedLocationClient: FusedLocationProviderClient
        var prefs: Prefs? = null
        lateinit var instance: MainActivity
            private set
    }

    // these shouldn't technically need to be here as they're suppressing warnings, but the code appears
    // to work fine including on an older actual device
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        prefs = Prefs(applicationContext)
        instance = this
        val currentDay = Math.floorMod((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2), 7)
        if (prefs!!.currentDay != currentDay) {
            wipeDay(prefs!!, prefs!!.currentDay)
            prefs!!.currentDay = currentDay
        }

        // Code that creates and sends notifications based on location. It's a lot of copy paste
        // from different sources, but it seems to work. However it's also the part of code with the
        // most issues that I want to change most as it doesn't have complete functionality.

        // creates a notification channel (it's fine to call it repeatedly, it just doesn't do anything
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "testName"
            val descriptionText = "testDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("testChannel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // Builds a specific notification (the only notification)
        val builder = NotificationCompat.Builder(this, "testChannel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Have you remembered everything?")
            .setContentText("You have not marked everything as packed.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // requests location every second
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getLocation { latitude, longitude ->
                    // very long if statement that checks if you've left for the first time and haven't
                    // completed everything
                    if (((kotlin.math.abs(prefs!!.latitude.toDouble() - latitude) > 0.0025) || (kotlin.math.abs(
                            prefs!!.longitude.toDouble() - longitude
                        ) > 0.0025)) && (itemsUnchecked(
                            prefs!!, prefs!!.currentDay
                        )) && (prefs!!.longitude.toDouble() != 0.0) && (longitude != 0.0) && (!prefs!!.remindedToday)
                    ) {
                        // Actually sends the notification
                        with(NotificationManagerCompat.from(this@MainActivity)) {
                            notify(0, builder.build())
                        }
                        prefs!!.remindedToday = true
                    }
                }
            }
        }, 0, 1000)
        // allows navigation between screens, with start destination of the home screen
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "homeScreen") {
                composable("homeScreen") {
                    HomeScreen(prefs = prefs!!, navigation = navController)
                }
                composable("displayTasks/{dayToDisplay}") { backStackEntry ->
                    val dayToDisplay = backStackEntry.arguments?.getString("dayToDisplay")
                    val newDay = dayToDisplay!!.toInt()
                    DisplayTasks(prefs = prefs!!, navigation = navController, day = newDay)
                }
                composable("addTask") {
                    AddTask(prefs = prefs!!, navigation = navController)
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
// Gets location, but only gets the last one another application asks for
// I doubt this would actually work generally so will need to change this function
fun getLocation(callback: (Double, Double) -> Unit) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val lat = location.latitude
            val long = location.longitude
            callback(lat, long)
        } else {
            callback(0.0, 0.0)
        }
    }
}