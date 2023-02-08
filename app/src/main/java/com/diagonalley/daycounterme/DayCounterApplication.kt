package com.diagonalley.daycounterme

import android.app.Application
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.utils.DebugTree
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DayCounterApplication : Application() {
    @Inject
    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
        }
        if (appConfig.isDebug()) {
            Timber.plant(DebugTree())
        }
    }
}