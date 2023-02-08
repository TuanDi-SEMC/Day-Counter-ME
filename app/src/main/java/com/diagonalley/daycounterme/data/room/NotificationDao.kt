package com.diagonalley.daycounterme.data.room

import androidx.room.Dao
import androidx.room.Query
import com.diagonalley.daycounterme.base.BaseDao
import com.diagonalley.daycounterme.data.room.model.MyNotification
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NotificationDao : BaseDao<MyNotification> {
    // Other insertion/deletion/query operations
    @Query("SELECT count(id) FROM items") // items is the table in the @Entity tag of ItemsYouAreStoringInDB.kt, id is a primary key which ensures each entry in DB is unique
    suspend fun numberOfItemsInDB(): Int // suspend keyword to run in coroutine

    @Query("SELECT * FROM items ORDER BY createTime DESC")
    fun getAllNotificationList(): Flow<List<MyNotification>>

    @Query("SELECT * FROM items WHERE id = :idNotification")
    fun getNotification(idNotification: Long): Flow<MyNotification?>

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    @Query("DELETE FROM items WHERE id = :idNotification")
    suspend fun deleteNotificationById(idNotification: Long)
}