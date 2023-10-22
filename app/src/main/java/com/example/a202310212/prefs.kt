package com.example.a202310212

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("1", Context.MODE_PRIVATE)
    private var stringPref = "allData"
    var mainData: String?
        get() = preferences.getString(stringPref, "")
        set(value) = preferences.edit().putString(stringPref, value).apply()
}