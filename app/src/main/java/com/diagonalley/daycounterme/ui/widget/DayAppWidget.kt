package com.diagonalley.daycounterme.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.request.target.AppWidgetTarget
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.global.Constant
import com.diagonalley.daycounterme.ui.activity.WidgetSettingsActivity
import com.diagonalley.daycounterme.utils.getBitmap
import timber.log.Timber

const val ACTION_REFRESH = "refresh"
const val ACTION_UPDATE_WIDGET = "update_widget"

class DayAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
        val prefs = context.getSharedPreferences(Constant.SHARED_PREPS, Context.MODE_PRIVATE)
        val defaultUrl = "https://sample-videos.com/img/Sample-jpg-image-100kb.jpg"
        val url = prefs.getString("url", null) ?: defaultUrl


        val configIntent = Intent(context, WidgetSettingsActivity::class.java)
        configIntent.action = ACTION_UPDATE_WIDGET
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val configPendingIntent = PendingIntent.getActivity(
            context,
            0,
            configIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("TuanTM", "onUpdate: $url\nappWidgetId: $appWidgetId")
        // Get the layout for the widget and attach an on-click listener
        // to the button.
        val remoteViews: RemoteViews = RemoteViews(
            context.packageName, R.layout.app_widget_provider_layout
        ).apply {
            setOnClickPendingIntent(R.id.img, configPendingIntent)
        }
        val awt: AppWidgetTarget =
            object : AppWidgetTarget(context, R.id.img, remoteViews, appWidgetId) {}
        awt.getBitmap(context, url)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    /**
     * Hàm được gọi khi widget được setup lần đầu tiên và mỗi khi thay đổi kích thước.
     * Sử dụng để ẩn, hiện nội dung khi kích thước của widget thay đổi.
     * */
    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?,
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Timber.d("")
    }

    override fun onReceive(context: Context, intent: Intent) {
        when {
            intent.action.equals(ACTION_REFRESH) &&
                    intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID) -> {
                val appWidgetManager = context.getSystemService(AppWidgetManager::class.java)
                updateWidget(
                    context,
                    appWidgetManager,
                    intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
                )
            }
            intent.action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED) -> {

            }
            else -> {
                super.onReceive(context, intent)
            }
        }
    }
}