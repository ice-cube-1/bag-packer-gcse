package com.example.a202310212

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


// screen that has some fields that are constantly updates, and the result of them is saved when the
// user presses submit
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddTask(prefs: Prefs, navigation: NavController) {
    // remember variables to refresh UI
    var goHome by remember { mutableStateOf(false) }
    var taskValue by remember { mutableStateOf("") }
    var recurringValue by remember { mutableStateOf(true) }
    val checkedState by remember { mutableStateOf(MutableList(7) { false }) }
    // when submit is pressed, stores the data and navigates back
    if (goHome) {
        if (taskValue != "") {
            for (i in 0..6) {
                if (checkedState[i]) {
                    addData(prefs, taskValue, i, false, recurringValue)
                }
            }
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add Task", fontSize = 20.sp, modifier = Modifier.padding(8.dp) , color = MaterialTheme.colorScheme.onBackground)
        // takes input of task name
        OutlinedTextField(value = taskValue,
            modifier = Modifier.padding(8.dp),
            label = { Text("Task name") },
            singleLine = true,
            onValueChange = { taskValue = it })
        // displays checkboxes so user can pick what days their task is valid for
        for (i in 0..6) {
            var individualCheck by remember { mutableStateOf(false) }
            Row {
                Checkbox(checked = individualCheck, colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary, uncheckedColor = MaterialTheme.colorScheme.onPrimary), onCheckedChange = {
                    individualCheck = it
                    checkedState[i] = it
                })
                Text(text = daysOfWeek[i], modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground)
            }
        }
        // displays toggle for if the task is recurring or not
        Row {
            Text("Recurring:", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground)
            Switch(checked = recurringValue, onCheckedChange = { recurringValue = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary, // Set the color for the thumb when the switch is on
                    checkedTrackColor = MaterialTheme.colorScheme.outline, // Set the color for the track when the switch is on
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary, // Set the color for the thumb when the switch is off
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline // Set the color for the track when the switch is off
                ))
        }
        // registers that the app should save the data inputted and go back to the main screen
        Button(onClick = { goHome = true }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primaryContainer)) {
            Text("Submit", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}