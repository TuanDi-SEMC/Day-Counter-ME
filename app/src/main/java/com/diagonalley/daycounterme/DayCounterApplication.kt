package com.diagonalley.daycounterme

import android.app.Application
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.utils.DebugTree
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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
        initFirebaseRemoteConfig()
        if (appConfig.isDebug()) {
            Timber.plant(DebugTree())
        }
    }
    private fun initFirebaseRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            if (BuildConfig.DEBUG) {
                val configSettings = remoteConfigSettings {
                    //todo: clear remote config setting when release
//                    minimumFetchIntervalInSeconds = 30
                }
                setConfigSettingsAsync(configSettings)
            }
            fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("Fetch and activate succeeded")
                } else {
                    Timber.e("Fetch failed")
                }
            }
        }
    }

}