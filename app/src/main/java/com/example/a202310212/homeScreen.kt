package com.example.a202310212

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// screen that displays days of the week that when clicked display the list for that day
// also has a + button to go to a page to add tasks
// also had a "set home location" button that does some background processing but does not display
// anything (should probably change this)
@Composable
fun HomeScreen(prefs: Prefs, navigation: NavController) {
    // remember variables to refresh page
    var goAddTask by remember { mutableStateOf(false) }
    var goViewList by remember { mutableIntStateOf(-1) }
    var snackbarVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var snackbarMessage by remember { mutableStateOf("") }
    // navigates to list page passing the correct argument
    if (goViewList != -1) {
        LaunchedEffect(Unit) {
            navigation.navigate(
                "displayTasks/{dayToGo}".replace(
                    oldValue = "{dayToGo}", newValue = goViewList.toString()
                )
            )
        }
    }
    // navigates to add task page
    if (goAddTask) {
        LaunchedEffect(Unit) {
            navigation.navigate("addTask")
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lists", fontSize = 20.sp)
        // displays a button for each day that will update "goViewList" with the day to view on press
        for (i in 0..6) {
            ElevatedButton(onClick = { goViewList = i }) {
                Text(daysOfWeek[i], modifier = Modifier.padding(4.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        // row with the add task button and a button that sets the home location in sharedPreferences
        Row {
            OutlinedIconButton(onClick = { goAddTask = true }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                snackbarMessage = if((kotlin.math.abs(prefs.latitude.toDouble() - prefs.latitudeCurrent) < 0.0005)
                    && (kotlin.math.abs(prefs.longitude.toDouble() - prefs.longitudeCurrent) < 0.0005)) {
                    "You are already at your home location"
                } else if (prefs.latitudeCurrent.toDouble() == 0.0) {
                    "Double check that your location is working"
                } else {
                    "Home location set"
                }
                prefs.latitude = prefs.latitudeCurrent
                prefs.longitude = prefs.longitudeCurrent
                snackbarVisible = true
                coroutineScope.launch {
                    delay(3000)
                    snackbarVisible = false
                }
                Log.d(prefs.latitude.toString(), prefs.longitude.toString())

            }) {
                Text("Set Home Location")
            }
        }
    }
    if (snackbarVisible) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            content = {
                Text(snackbarMessage)
            }
        )
    }
}