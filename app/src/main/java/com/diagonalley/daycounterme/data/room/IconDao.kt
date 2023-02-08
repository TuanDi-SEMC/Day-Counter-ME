package com.diagonalley.daycounterme.data.room

import android.graphics.drawable.Icon
import androidx.room.Dao
import androidx.room.Query
import com.diagonalley.daycounterme.base.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface IconDao : BaseDao<Icon> {
    @Query("SELECT * FROM icons ORDER BY createTime DESC")
    fun getIconList(): Flow<List<Icon>>

    @Query("SELECT * FROM icons  WHERE favourite =:isFavourite ORDER BY createTime DESC")
    fun getFavouriteIconList(isFavourite: Boolean): Flow<List<Icon>>

    @Query("SELECT * FROM icons ORDER BY createTime DESC LIMIT :top")
    fun getTop(top: Int = 10): Flow<List<Icon>>

    @Query("SELECT * FROM icons WHERE documentId =:id")
    fun getIconById(id: String): Icon?

    suspend fun insertOrUpdate(icons: ArrayList<Icon>) {
        icons.forEach {
//            val isFavourite: Boolean = getIconById(it.documentId)?.favourite ?: false
//            it.favourite = isFavourite
            insert(it)
        }
    }

    @Query("UPDATE icons set favourite=:isFavourite WHERE documentId=:documentId")
    suspend fun setFavourite(
        documentId: String,
        isFavourite: Boolean
    )
}