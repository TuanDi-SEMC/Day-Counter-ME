package com.diagonalley.daycounterme.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.global.AppConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @Inject
    lateinit var appConfig: AppConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}