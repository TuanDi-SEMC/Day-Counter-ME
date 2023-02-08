package com.diagonalley.daycounterme.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diagonalley.daycounterme.global.Constant.DATABASE_VERSION
import com.diagonalley.daycounterme.data.room.converter.BitmapConverters
import com.diagonalley.daycounterme.data.room.converter.DateConverters
import com.diagonalley.daycounterme.data.room.converter.StatusConverters
import com.diagonalley.daycounterme.data.room.converter.UriConverters
import com.diagonalley.daycounterme.data.room.model.MyNotification

@Database(
    entities = [MyNotification::class], version = DATABASE_VERSION, exportSchema = false
)
@TypeConverters(value = [DateConverters::class, BitmapConverters::class, UriConverters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao
}