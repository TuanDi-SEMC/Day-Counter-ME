package com.diagonalley.daycounterme.data.room.converter

import androidx.room.TypeConverter

/**
 * Type converters to allow Room to reference complex data types.
 */
const val PENDING_CODE = 0
const val CANCEL_CODE = 1
const val SUCCESS_CODE = 2
const val FAILED_CODE = 3

class StatusConverters {
//    @TypeConverter
//    fun fromStatus(value: Int): NotifyStatus {
//        return when (value) {
//            PENDING_CODE -> NotifyStatus.PENDING
//            CANCEL_CODE -> NotifyStatus.CANCEL
//            SUCCESS_CODE -> NotifyStatus.SUCCESS
//            else -> NotifyStatus.FAILED
//        }
//    }
//
//    @TypeConverter
//    fun codeToStatus(status: NotifyStatus): Int {
//        return when (status) {
//            NotifyStatus.PENDING -> PENDING_CODE
//            NotifyStatus.CANCEL -> CANCEL_CODE
//            NotifyStatus.SUCCESS -> SUCCESS_CODE
//            else -> FAILED_CODE
//        }
//    }
}
