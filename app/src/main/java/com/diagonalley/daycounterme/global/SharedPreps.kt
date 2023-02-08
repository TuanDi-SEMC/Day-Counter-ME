package com.diagonalley.daycounterme.global

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val FIRST_RUN = "first-run"
const val THEME_SETTING = "theme-setting"
const val VERSION_WARNING = "version-warning"

class SharedPreps(private val sharedPreferences: SharedPreferences) {

    var theme: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        get() {
            return sharedPreferences.getInt(
                THEME_SETTING,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
        set(value) {
            field = value
            sharedPreferences.edit().putInt(THEME_SETTING, value).apply()
        }

    var firstRun: Boolean = true
        get() {
            return sharedPreferences.getBoolean(FIRST_RUN, true)
        }
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(FIRST_RUN, value).apply()
        }

    var notWarningAndroidS: Boolean = false
        get() {
            return sharedPreferences.getBoolean(VERSION_WARNING, false)
        }
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(VERSION_WARNING, value).apply()
        }
}