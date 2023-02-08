package com.diagonalley.daycounterme.data.room.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "items")
data class MyNotification(
    var title: String,
    var appName: String,
    var content: String? = null,
    var createTime: Date,
    var appIcon: Bitmap,
    var uri: Uri? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var deliveryTime: Date? = null
}
