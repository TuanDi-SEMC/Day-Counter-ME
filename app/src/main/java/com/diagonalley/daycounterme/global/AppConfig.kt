package com.diagonalley.daycounterme.global

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.diagonalley.daycounterme.BuildConfig
import com.diagonalley.daycounterme.global.Constant.CONFIG_IN_REVIEW
import com.diagonalley.daycounterme.global.Constant.CONFIG_SHOW_AD
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class AppConfig(private val sharedPreps: SharedPreps, resource: Resources) {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val currentVersion: Int = BuildConfig.VERSION_CODE
    private val minVersion = remoteConfig.getLong(Constant.CONFIG_MIN_VERSION_CODE)
    private val latestVersion = remoteConfig.getLong(Constant.CONFIG_LATEST_VERSION_CODE)
    private val reviewVersion = remoteConfig.getLong(Constant.CONFIG_REVIEW_VERSION_CODE)
    private val showAd = remoteConfig.getBoolean(CONFIG_SHOW_AD)
    fun hasNewUpdate(): Boolean = currentVersion < latestVersion
    fun foreUpdate(): Boolean = currentVersion < minVersion
    fun isDebug(): Boolean = BuildConfig.DEBUG
    fun inReview(): Boolean {
        return currentVersion.toLong() == reviewVersion && remoteConfig.getBoolean(CONFIG_IN_REVIEW)
    }

    /*
    Chỉ ẩn ad khi đang trạng thái review và currentVerion ==
    * */
    fun isShowAd() = showAd

    var theme: Int
        get() = sharedPreps.theme
        set(value) {
            sharedPreps.theme = value
            AppCompatDelegate.setDefaultNightMode(value)
        }
}