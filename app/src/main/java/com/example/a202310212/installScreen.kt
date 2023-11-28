package com.example.a202310212

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InstallScreen(navigation: NavController, prefs: Prefs) {
    var checked by remember { mutableStateOf(false) }
    var nextScreen by remember { mutableStateOf(false) }
    // navigates to the home screen
    if (nextScreen) {
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add Image for the header
        Image(
            painter = painterResource(id = R.drawable.header_image),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add title text box
        Text(
            text = "Welcome to Forget me not", style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // description text box
        Text(
            text = "Use this app to help with your everyday tasks. " + "Our app allows you to add items that they need to bring either weekly or as a one-off and mark them as completed. " + "If you haven't marked everything for the day as completed and leave the house a notification reminds them to check if they have remembered everything. " + "On the home screen the user has the option to view the lists for each day, add a task or set their home location! ",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // additional info text box
        Text(
            text = "Each day of the week is laid out beneath 'Lists'. " + "Click onto each day to view and add to your daily lists: you can add, remove, edit, and check off any item. " + "Never forget anything at home by setting your home location - click the button in the bottom right to set! " + "Set tasks as recurring for that day every week in the task addition page, a recurring icon will appear beside your task - this can be removed at any time! " + "Lastly, personalise your Forget me not experience with app colour customisation: use the hearts in the bottom half of the screen as toggles for your colour changes! " + "Have fun, be organised, and DON'T FORGET YOUR LANYARD! :) ",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(24.dp)) {
            // changes shared preferences value as to whether this screen should be shown
            Checkbox(checked = checked, onCheckedChange = {
                prefs.showInstallScreen = false
                checked = it
            })
            Text(text = "Don't show again")
            Spacer(modifier = Modifier.weight(1f))
            // navigates to the home screen
            Button(onClick = { nextScreen = true }) {
                Text("OK")
            }
        }
    }

}
