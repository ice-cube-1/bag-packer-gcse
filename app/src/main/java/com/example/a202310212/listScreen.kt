package com.example.a202310212

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
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

// displays a single item in the task list and allows the main function to act on the buttons being
// pressed or checked
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
            checked = checked, onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline
            ),
        )
        Text(
            text = taskName,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        if (recurring) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Recurring",
                modifier = Modifier.padding(12.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onClose) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}

// Displays a tasks list with a "go back" button that saves any changes to number of tasks (checks
// are saved immediately, which can cause weird bugs where the UI is refreshed and removed tasks
// reappear but recently checked tasks stay checked. However, this is an edge use case so is
// probably fine
@SuppressLint("MutableCollectionMutableState")
@Composable
fun DisplayTasks(prefs: Prefs, modifier: Modifier = Modifier, navigation: NavController, day: Int) {
    // var by remembers for UI refresh
    var goHome by remember { mutableStateOf(false) }
    val removable by remember { mutableStateOf(mutableListOf<ItemToPack>()) }
    val tasks = readDataForDay(prefs, day)
    // removes tasks user has removed and navigates back to the home screen
    if (goHome) {
        for (i in removable) {
            removeData(prefs, i.day, i.completed, i.item, i.recurring)
        }
        LaunchedEffect(Unit) {
            navigation.navigate("homeScreen")
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = daysOfWeek[day], fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground
        )
        for (i in tasks) {
            // more UI refresher vars, this time to show if an individual task is shown / checked.
            // this is just related to the UI, and does not change underlying data at all
            // recurring cannot be changed, it is just an icon so the user knows the status of the task
            var showTask by remember { mutableStateOf(true) }
            var checked by remember { mutableStateOf(i.completed) }
            if (showTask) {
                TaskItem(taskName = i.item, checked = checked, onCheckedChange = {
                    checked = it
                    // what actually changes the tasks completion state in the store data
                    changeCompletion(prefs, i)
                    i.completed = it
                }, onClose = {
                    showTask = false
                    // added to the list of tasks to remove
                    removable.add(i)
                }, recurring = i.recurring
                )
            }
        }
        // once clicked will remove tasks and go to home screen
        Button(
            onClick = { goHome = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text("Go Back", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}