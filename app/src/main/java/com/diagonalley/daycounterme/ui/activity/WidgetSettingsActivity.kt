package com.diagonalley.daycounterme.ui.activity

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.diagonalley.daycounterme.databinding.ActivityWidgetSettingsBinding
import com.diagonalley.daycounterme.global.Constant
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.ui.widget.ACTION_UPDATE_WIDGET
import com.diagonalley.daycounterme.ui.widget.DayAppWidget
import com.diagonalley.daycounterme.utils.updateWidget
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class WidgetSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWidgetSettingsBinding
    private var appWidgetId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWidgetSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val action = intent.action
        if (action == ACTION_UPDATE_WIDGET) {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            Timber.d("App widget id: $appWidgetId")
            binding.tvTitle.text = "Action: $action\nApp widget id: $appWidgetId"
        }

        binding.apply {
            btnUpdate.setOnClickListener {
                //todo: Update widget
                val prefs = this@WidgetSettingsActivity.getSharedPreferences(
                    Constant.SHARED_PREPS, Context.MODE_PRIVATE
                )
                val size = (500..1000).random()
                val edit = prefs.edit()
                edit.putString("url", "https://picsum.photos/$size")
                edit.putString("edit", "WidgetSettingsActivity")
                edit.apply()
                appWidgetId?.let { widgetId ->
                    this@WidgetSettingsActivity.updateWidget(
                        widgetClass = DayAppWidget::class.java, appWidgetId = widgetId
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        //Back to main activity
        MainActivity.clearAndStart(this)
    }
}