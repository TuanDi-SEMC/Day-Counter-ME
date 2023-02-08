package com.diagonalley.daycounterme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.diagonalley.daycounterme.global.AppConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appConfig: AppConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}