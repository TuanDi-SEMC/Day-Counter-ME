package com.diagonalley.daycounterme.global

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.diagonalley.daycounterme.data.model.UserInfo
import com.google.gson.Gson
import javax.inject.Inject

const val FIRST_RUN = "first-run"
const val THEME_SETTING = "theme-setting"
const val VERSION_WARNING = "version-warning"
const val BIOMETRIC_SETTING = "biometric-settings"
const val USER_INFO = "user-info"

class SharedPreps @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    val gson: Gson,
) {

    var theme: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        get() {
            return sharedPreferences.getInt(
                THEME_SETTING, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
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

    var url: String? = null
        get() {
            return sharedPreferences.getString("url", null)
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString("url", value).apply()
        }

    var isBiometricEnabled: Boolean = false
        get() {
            return sharedPreferences.getBoolean(BIOMETRIC_SETTING, false)
        }
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(BIOMETRIC_SETTING, value).apply()
        }

    var userInfo: UserInfo? = null
        get() {
            val data = sharedPreferences.getString(USER_INFO, null)
            return gson.fromJson(data, UserInfo::class.java)
        }
        set(value) {
            field = value
            val data = gson.toJson(value)
            sharedPreferences.edit().putString(USER_INFO, data).apply()
        }
}