package com.example.a202310212

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
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
import com.example.a202310212.ui.theme.md2_theme_light_primary
import com.example.a202310212.ui.theme.md3_theme_light_primary
import com.example.a202310212.ui.theme.md_theme_light_primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// screen that displays days of the week that when clicked display the list for that day
// also has a + button to go to a page to add tasks
// also had a "set home location" button that does some background processing but does not display
// anything (should probably change this)
@Composable
fun HomeScreen(prefs: Prefs, navigation: NavController, updateTheme: (Int) -> Unit) {
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
        Text("Days", fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
        // displays a button for each day that will update "goViewList" with the day to view on press
        for (i in 0..6) {
            ElevatedButton(onClick = { goViewList = i }) {
                Text(
                    daysOfWeek[i],
                    modifier = Modifier.padding(4.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            // add task button
            IconButton(
                onClick = { goAddTask = true },
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            // three icon buttons that each update the theme to have the primary colour of the icon
            // the theme will also be updated if the user switches to dark mode
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                updateTheme(0)
                prefs.colorScheme=0
            }) {
                Icon(
                    Icons.Filled.Favorite, contentDescription = null, tint = md_theme_light_primary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                updateTheme(1)
                prefs.colorScheme=1
            }) {
                Icon(
                    Icons.Filled.Favorite, contentDescription = null, tint = md2_theme_light_primary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                updateTheme(2)
                prefs.colorScheme=2
            }) {
                Icon(
                    Icons.Filled.Favorite, contentDescription = null, tint = md3_theme_light_primary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // set home location button
            Button(
                onClick = {
                    // sets the snackbar message
                    snackbarMessage = if (prefs.latitudeCurrent.toDouble() == 0.0) {
                        "Allow location permissions in settings"
                    } else if ((kotlin.math.abs(prefs.latitude.toDouble() - prefs.latitudeCurrent) < 0.0005) && (kotlin.math.abs(
                            prefs.longitude.toDouble() - prefs.longitudeCurrent
                        ) < 0.0005)
                    ) {
                        "You are already at your home location"
                    } else {
                        "Home location set"
                    }
                    prefs.latitude = prefs.latitudeCurrent
                    prefs.longitude = prefs.longitudeCurrent
                    // makes the snackbar visible for 1 sec
                    snackbarVisible = true
                    coroutineScope.launch {
                        delay(3000)
                        snackbarVisible = false
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text("Set Home Location", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
    // actually shows the snackbar
    if (snackbarVisible) {
        Snackbar(modifier = Modifier.padding(16.dp),
            backgroundColor = MaterialTheme.colorScheme.onSurface,
            content = {
                Text(snackbarMessage, color = MaterialTheme.colorScheme.surfaceVariant)
            })
    }
}