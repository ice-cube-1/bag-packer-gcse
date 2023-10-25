# bag-packer-gcse
App that allows the user to add items that they need to bring either weekly or as a one-off and mark them as completed.

If they haven't marked everything for the day as completed and leave the house a notification reminds them to check if they have remembered everything.

NOTE: For this app to work you will need to manually enable location permissions "while using the app" after install. I am not planning to implement the feature where it requests permissions.

___

On the home screen the user has the option to view the lists for each day, add a task or set their home location
 - Each task list shows the tasks, whether they are recurring and / or completed, and for each task gives the user the option to complete or remove them
 - Pressing add task goes to a screen where the user can input the name of the task, what day it is on and whether or not it is recurring, which is then stored when they press submit. If the task name is empty, nothing is store.
 - Pressing set home location stores the users current location as their home
   
The first time in a day the app is opened all tasks from the previous day are marked as incomplete and all non-recurring tasks will be deleted

The first time in a day the user leaves the area near the location they set as home it will send a push notification if they have incomplete tasks

___

Current issues / features to be implemented (in order of priority)
 - #07: Add ontap function for location to either open the app or view the precise day (depending how hard it is)
 - #06: Change daily refresh so it works if the app isn't opened every day (details of how to fix in the issue)
 - #09: Some kind of acknowlegement the home location is set (not sure what yet)
 - #12: Adding colour schemes / icon / correct app name - important but given I don't yet know even the name it's not high priority
 - #10: Location permissions - as mentioned above it would be useful but there's a workaround and its quite a lot of work
 - #03: Tasks reappearing on screen rotate - such an edge case it should be fine
