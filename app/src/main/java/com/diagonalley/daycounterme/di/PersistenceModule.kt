package com.diagonalley.daycounterme.di

import android.content.Context
import androidx.room.Room
import com.diagonalley.daycounterme.data.room.AppDatabase
import com.diagonalley.daycounterme.global.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context,
    ) = Room.databaseBuilder(
        app, AppDatabase::class.java, DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideNotificationDao(db: AppDatabase) = db.getNotificationDao()
}
