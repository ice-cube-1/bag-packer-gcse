package com.example.a202310212

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a202310212.MainActivity.Companion.fusedLocationClient
import com.example.a202310212.MainActivity.Companion.locationCallback
import com.example.a202310212.MainActivity.Companion.locationRequest
import com.example.a202310212.ui.theme.DarkColors1
import com.example.a202310212.ui.theme.DarkColors2
import com.example.a202310212.ui.theme.DarkColors3
import com.example.a202310212.ui.theme.LightColors1
import com.example.a202310212.ui.theme.LightColors2
import com.example.a202310212.ui.theme.LightColors3
import com.example.a202310212.ui.theme.WoofTheme
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Calendar

// class for each item that will be added to the list of items, which is then serialized into preferences
data class ItemToPack(
    var day: Int, var item: String, var completed: Boolean, var recurring: Boolean
)

// for serializing / deserializing in future
val JSON = jacksonObjectMapper()

// for the text that converts 0-6 into monday-sunday
val daysOfWeek =
    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class MainActivity : ComponentActivity() {
    // initializes global variables for the location and preferences
    companion object {
        lateinit var fusedLocationClient: FusedLocationProviderClient
        var prefs: Prefs? = null
        lateinit var instance: MainActivity
            private set
        val locationRequest =
            LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 5000).build()
        lateinit var locationCallback: LocationCallback
    }

    // these shouldn't technically need to be here as they're suppressing warnings, but the code appears
    // to work fine including on an older actual device
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Prefs(applicationContext)
        instance = this
        // wipes days if required
        val currentDay = Math.floorMod((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2), 7)
        if (prefs!!.currentDay < currentDay) {
            for (i in prefs!!.currentDay..<currentDay) {
                wipeDay(prefs!!, i)
            }
        } else if (prefs!!.currentDay > currentDay) {
            for (i in prefs!!.currentDay..6) {
                wipeDay(prefs!!, i)
            }
            for (i in 0..<currentDay) {
                wipeDay(prefs!!, i)
            }
        }
        wipeDay(prefs!!, prefs!!.currentDay)
        prefs!!.currentDay = currentDay

        // Code that creates and sends notifications based on location. It's a lot of copy paste
        // from different sources, but it seems to work. However it's also the part of code with the
        // most issues that I want to change most as it doesn't have complete functionality.

        // creates a notification channel (it's fine to call it repeatedly, it just doesn't do anything)
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
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "testChannel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Have you remembered everything?")
            .setContentText("You have not marked everything as packed.")
            .setPriority(NotificationCompat.PRIORITY_MAX).setContentIntent(pendingIntent)
        // requests location repeatedly and stores it
        // also sends a notification on specific conditions
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0
                for (location in p0.locations) {
                    Log.d("location", location.latitude.toString())
                    prefs!!.longitudeCurrent = location.longitude.toFloat()
                    prefs!!.latitudeCurrent = location.latitude.toFloat()
                    // very long if statement that checks if you've left for the first time and haven't
                    // completed everything
                    if (((kotlin.math.abs(prefs!!.latitude.toDouble() - location.latitude) > 0.0005) || (kotlin.math.abs(
                            prefs!!.longitude.toDouble() - location.longitude
                        ) > 0.0005)) && (itemsUnchecked(
                            prefs!!, prefs!!.currentDay
                        )) && (prefs!!.longitude.toDouble() != 0.0) && (location.longitude != 0.0) && !prefs!!.remindedToday
                    ) {
                        // Actually sends the notification
                        prefs!!.remindedToday = true
                        Log.d("notifying", location.latitude.toString())
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(0, builder.build())
                    }
                }
            }
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
        var lightColorScheme: ColorScheme
        var darkColorScheme: ColorScheme
        // allows navigation between screens, with start destination of the home screen
        setContent {
            var isTheme1 by remember { mutableIntStateOf(prefs!!.colorScheme) }
            when (isTheme1) {
                0 -> {
                    lightColorScheme = LightColors1
                    darkColorScheme = DarkColors1
                }

                1 -> {
                    lightColorScheme = LightColors2
                    darkColorScheme = DarkColors2
                }

                else -> {
                    lightColorScheme = LightColors3
                    darkColorScheme = DarkColors3
                }
            }
            WoofTheme(
                customDarkColorScheme = darkColorScheme, customLightColorScheme = lightColorScheme
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    var startScreen = "homeScreen"
                    if (prefs!!.showInstallScreen) {
                        startScreen = "installScreen"
                    }
                    NavHost(navController = navController, startDestination = startScreen) {
                        composable("homeScreen") {
                            HomeScreen(
                                prefs = prefs!!,
                                navigation = navController,
                                updateTheme = { themeMode -> isTheme1 = themeMode },
                                isTheme1 = isTheme1
                            )
                        }
                        composable("displayTasks/{dayToDisplay}") { backStackEntry ->
                            val dayToDisplay = backStackEntry.arguments?.getString("dayToDisplay")
                            val newDay = dayToDisplay!!.toInt()
                            DisplayTasks(prefs = prefs!!, navigation = navController, day = newDay)
                        }
                        composable("installScreen") {
                            InstallScreen(navigation = navController, prefs=prefs!!)
                        }
                        composable("addTask") {
                            AddTask(prefs = prefs!!, navigation = navController)
                        }
                    }
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }
}

// starts location updates on app opening
@SuppressLint("MissingPermission")
private fun startLocationUpdates() {
    fusedLocationClient.requestLocationUpdates(
        locationRequest, locationCallback, Looper.getMainLooper()
    )
}