package com.diagonalley.daycounterme.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.diagonalley.daycounterme.broadcast.MyBroadcastReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val EXACT_ALARM_INTENT_REQUEST_CODE = 111

class MyAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
) {
    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            return true
        }
    }

    fun gotoAlarmSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM

            }
            context.startActivity(intent)
        }
    }

    fun scheduleAnAlarm(triggerAtMillis: Long) {
        // 1
        val pendingIntent = createExactAlarmIntent()
        // 2
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    private fun createExactAlarmIntent(): PendingIntent {
        // 1
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        // 2
        return PendingIntent.getBroadcast(
            context, EXACT_ALARM_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }
}