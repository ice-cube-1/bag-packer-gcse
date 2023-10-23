package com.example.a202310212

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("1", Context.MODE_PRIVATE)
    private var alldatapref = "allData"
    private  var daypref = "currentDayStuff"
    private var boolpref = "ReminderStuff"
    private var latpref = "latpref"
    private var longpref = "longpref"
    var mainData: String?
        get() = preferences.getString(alldatapref, JSON.writeValueAsString(listOf<perDayEntry>()))
        set(value) = preferences.edit().putString(alldatapref, value).apply()
    var currentDay: Int
        get() = preferences.getInt(daypref,0)
        set(value) = preferences.edit().putInt(daypref,value).apply()
    var remindedToday: Boolean
        get() = preferences.getBoolean(boolpref,false)
        set(value) = preferences.edit().putBoolean(boolpref,value).apply()
    var latitude: Float
        get() = preferences.getFloat(latpref, 0.0F)
        set(value) = preferences.edit().putFloat(latpref,value).apply()
    var longitude: Float
        get() = preferences.getFloat(longpref, 0.0F)
        set(value) = preferences.edit().putFloat(longpref,value).apply()
}