package com.example.a202310212

import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.module.kotlin.readValue

// Class that can easily be uploaded to sharedPreferences
// no idem can contain a list, which is why "tasksList" is a serialized list of data objects
class Prefs(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("1", Context.MODE_PRIVATE)
    private var allDataPref = "allData"
    private var dayPref = "currentDayStuff"
    private var boolPref = "ReminderStuff"
    private var latPref = "latPref"
    private var longPref = "longPref"
    private var latPrefCurrent = "latPrefCurrent"
    private var longPrefCurrent = "longPrefCurrent"
    var tasksList: String?
        get() = preferences.getString(allDataPref, JSON.writeValueAsString(listOf<ItemToPack>()))
        set(value) = preferences.edit().putString(allDataPref, value).apply()
    var currentDay: Int
        get() = preferences.getInt(dayPref, 0)
        set(value) = preferences.edit().putInt(dayPref, value).apply()
    var remindedToday: Boolean
        get() = preferences.getBoolean(boolPref, false)
        set(value) = preferences.edit().putBoolean(boolPref, value).apply()
    var latitude: Float
        get() = preferences.getFloat(latPref, 0.0F)
        set(value) = preferences.edit().putFloat(latPref, value).apply()
    var longitude: Float
        get() = preferences.getFloat(longPref, 0.0F)
        set(value) = preferences.edit().putFloat(longPref, value).apply()
    var latitudeCurrent: Float
        get() = preferences.getFloat(latPrefCurrent, 0.0F)
        set(value) = preferences.edit().putFloat(latPrefCurrent, value).apply()
    var longitudeCurrent: Float
        get() = preferences.getFloat(longPrefCurrent, 0.0F)
        set(value) = preferences.edit().putFloat(longPrefCurrent, value).apply()
}

// these functions should be self-explanatory
// while preferences are directly accessed, the "tasksList" variable is not accessed from other code
// to prevent confusion with serialization / deserialization
// in terms of complexity it's not great as it's reading, deserializing, editing, serializing and
// writing the entire dataset to a single cell in SQLite multiple times in a single button press
// however that should be fine given how small the dataset is

fun readData(prefs: Prefs): MutableList<ItemToPack> {
    return JSON.readValue(prefs.tasksList!!)
}

// these all edit the data slightly and write it back
fun changeCompletion(prefs: Prefs, toChange: ItemToPack) {
    val entries = readData(prefs)
    entries[entries.indexOf(toChange)].completed = !entries[entries.indexOf(toChange)].completed
    prefs.tasksList = JSON.writeValueAsString(entries)
}

fun removeData(prefs: Prefs, day: Int, completed: Boolean, item: String, recurring: Boolean) {
    val entries = readData(prefs)
    entries.remove(ItemToPack(day, item, completed, recurring))
    prefs.tasksList = JSON.writeValueAsString(entries)
}

fun addData(prefs: Prefs, item: String, day: Int, completed: Boolean, recurring: Boolean) {
    var entries = readData(prefs)
    entries.add(ItemToPack(day, item, completed, recurring))
    entries = entries.distinct().toMutableList()
    prefs.tasksList = JSON.writeValueAsString(entries)

}

fun readDataForDay(prefs: Prefs, day: Int): MutableList<ItemToPack> {
    val entries = readData(prefs)
    val subsetEntries = mutableListOf<ItemToPack>()
    for (i in entries) {
        if (i.day == day) {
            subsetEntries.add(i)
        }
    }
    return subsetEntries
}

fun itemsUnchecked(prefs: Prefs, day: Int): Boolean {
    val entries = readData(prefs)
    for (i in entries) {
        if ((i.day == day) && (!i.completed)) {
            return true
        }
    }
    return false
}

// this one also edits other parts of prefs aside from tasksList, and is called the first time the
// app is opened per day
fun wipeDay(prefs: Prefs, day: Int) {
    prefs.remindedToday = false
    val entries = readData(prefs)
    for (i in 0..<entries.size) {
        if (entries[i].day == day) {
            entries[i].completed = false
        }
    }
    val newEntries = mutableListOf<ItemToPack>()
    for (i in entries) {
        if ((i.day != day) or (i.recurring)) {
            newEntries.add(i)
        }
    }
    prefs.tasksList = JSON.writeValueAsString(newEntries)
}