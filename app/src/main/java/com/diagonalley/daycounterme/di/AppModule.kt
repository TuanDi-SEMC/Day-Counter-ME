package com.diagonalley.daycounterme.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.global.Constant.SHARED_PREPS
import com.diagonalley.daycounterme.global.SharedPreps
import com.diagonalley.daycounterme.manager.MyAlarmManager
import com.diagonalley.daycounterme.manager.MyBiometricManager
import com.diagonalley.daycounterme.manager.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by TuanTM on 1/6/21.
 * Module
 * Component
 * Provides
 * Inject
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideResource(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * @Link: https://developer.android.com/topic/security/best-practices#permissions-share-data
     * */
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREPS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(sharedPreferences: SharedPreferences, gson: Gson): SharedPreps {
        return SharedPreps(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideAppConfig(sharedPreps: SharedPreps, resources: Resources): AppConfig {
        return AppConfig(sharedPreps, resources)
    }

    @Provides
    @Singleton
    fun provideAuthManager(sharedPreps: SharedPreps, appConfig: AppConfig): SessionManager {
        return SessionManager(sharedPreps, appConfig)
    }

    @Provides
    @Singleton
    fun provideMyBiometricManager(@ApplicationContext context: Context): MyBiometricManager {
        return MyBiometricManager(context)
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideMyAlarmManager(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
    ): MyAlarmManager {
        return MyAlarmManager(context, alarmManager)
    }
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}

