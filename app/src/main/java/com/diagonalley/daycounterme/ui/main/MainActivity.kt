package com.diagonalley.daycounterme.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.diagonalley.daycounterme.databinding.ActivityMainBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.global.SharedPreps
import com.diagonalley.daycounterme.manager.MyBiometricManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        fun clearAndStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var myBiometricManager: MyBiometricManager

    @Inject
    lateinit var preps: SharedPreps
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                }
                doubleBackToExitPressedOnce = true
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false }, 2000
                )
                Toast.makeText(
                    this@MainActivity, "Please click BACK again to exit", Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.apply {
//            btnUpdate.setOnClickListener {
//                val extras: Bundle? = intent.extras
//                val appWidgetId = extras?.getInt(
//                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
//                )
//                val defaultUrl =
//                    "https://cdn.dribbble.com/users/12642230/avatars/normal/data?1659336992"
//                preps.url = defaultUrl
//
//                appWidgetId?.let { widgetId ->
//                    this@MainActivity.updateWidget(
//                        widgetClass = DayAppWidget::class.java, appWidgetId = widgetId
//                    )
//                }
//                myBiometricManager.isBiometricEnable { isEnabled: Boolean, message: String, notSetup: Boolean?, intent: Intent? ->
//                    Timber.d("Biometric is enable: $isEnabled\nMessage: $message\nNotSetup: $notSetup")
//                    intent?.let { it1 -> startActivity(it1) }
//
//                    if (isEnabled) {
//                        myBiometricManager.authenticateByBiometric(
//                            this@MainActivity,
//                            object : BiometricAuthenticationListener {
//                                override fun onSuccess() {
//                                    Toast.makeText(
//                                        this@MainActivity,
//                                        "Biometric authentication is successful!",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                }
//
//                                override fun onFailure() {
//                                    Toast.makeText(
//                                        this@MainActivity,
//                                        "Biometric is failure, please try again!",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                }
//                            })
//                    }
//                }
//            }
//            onBackPressedDispatcher.addCallback(this@MainActivity, callback)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}