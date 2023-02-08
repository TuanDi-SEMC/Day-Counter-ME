package com.diagonalley.daycounterme.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.global.Constant.SHARED_PREPS
import com.diagonalley.daycounterme.global.SharedPreps
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
    fun provideSharedPrefs(sharedPreferences: SharedPreferences): SharedPreps {
        return SharedPreps(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAppConfig(sharedPreps: SharedPreps, resources: Resources): AppConfig {
        return AppConfig(sharedPreps, resources)
    }
}