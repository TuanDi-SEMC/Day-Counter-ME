package com.diagonalley.daycounterme.ui.splash

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import com.afollestad.materialdialogs.MaterialDialog
import com.diagonalley.daycounterme.MainActivity
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseActivity
import com.diagonalley.daycounterme.databinding.ActivitySplashBinding
import com.diagonalley.daycounterme.ext.openAppSystemSettings
import com.diagonalley.daycounterme.ext.openGoogleStore
import com.diagonalley.daycounterme.global.ADMOD_TAG
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.global.SharedPreps
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    val viewModel: SplashViewModel by viewModels()
    private var mInterstitialAd: InterstitialAd? = null
    override fun getLayoutId(): Int = R.layout.activity_splash

    @Inject
    lateinit var sharedPrefs: SharedPreps

    @Inject
    lateinit var appConfig: AppConfig
    private val showAd by lazy { appConfig.isShowAd() }

    private val launcher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                checkForUpdate()
            } else {
                MaterialDialog(this).show {
                    title(R.string.required_permission)
                    message(text = "Please grant Notification permission from App Settings")
                    positiveButton(R.string.take_me_to_settings) {
                        openAppSystemSettings()
                    }
                    negativeButton(R.string.cancel) {
                        finish()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.event.observe(this) {
            when (it) {
                SplashEvent.CHECKED_VERSION -> {
                    moveForward()
                }
                SplashEvent.AD_INITIALIZED -> {
                    createAdRequest()
                }
                SplashEvent.SHOW_AD_FAILED, SplashEvent.LOAD_AD_FAILED, SplashEvent.DISMISS_AD -> {
                    gotoMainActivity()
                }
                SplashEvent.LOAD_AD_SUCCESS -> {
                    showAd()
                }
                else -> Unit
            }
        }
        binding.apply {
            animationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        checkForUpdate()
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
        }
    }


    private fun checkForUpdate() {
        if (appConfig.hasNewUpdate()) {
            if (appConfig.foreUpdate()) {
                onUpdateNeeded(false)
            } else {
                onUpdateNeeded(true)
            }
        } else {
            moveForward()
        }
    }

    private fun onUpdateNeeded(isMandatoryUpdate: Boolean) {
        MaterialDialog(this).show {
            title(R.string.app_update_available)
            positiveButton(R.string.update_now) {
                openGoogleStore()
                finish()
            }
            if (isMandatoryUpdate) {
                message(R.string.fore_update)
                negativeButton(R.string.action_skip) {
                    moveForward()
                }
            } else {
                message(R.string.suggest_update)
            }
        }
    }

    private fun moveForward() {
        if (showAd) {
            MobileAds.initialize(this@SplashActivity) {
                viewModel.setEvent(SplashEvent.AD_INITIALIZED)
            }
        } else {
            gotoMainActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.firstRun = false
    }

    private fun showAd() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Timber.tag(ADMOD_TAG).d("Interstitial ad was dismissed.")
                viewModel.setEvent(SplashEvent.DISMISS_AD)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Timber.tag(ADMOD_TAG).d("Interstitial ad failed to show.")
                viewModel.setEvent(SplashEvent.SHOW_AD_FAILED)
            }

            override fun onAdShowedFullScreenContent() {
                Timber.tag(ADMOD_TAG).d("Interstitial ad showed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Timber.tag(ADMOD_TAG).d("Interstitial ad have clicked.")
            }
        }
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this@SplashActivity)
        } else {
            Timber.tag(ADMOD_TAG).d("Interstitial ad wasn't ready yet.")
        }
    }

    private fun createAdRequest() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,
            getString(R.string.interstitial_ads_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.tag(ADMOD_TAG).d("Interstitial ad error: ${adError.message}")
                    mInterstitialAd = null
                    viewModel.setEvent(SplashEvent.LOAD_AD_FAILED)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Timber.tag(ADMOD_TAG).d("Interstitial ad was loaded")
                    mInterstitialAd = interstitialAd
                    viewModel.setEvent(SplashEvent.LOAD_AD_SUCCESS)
                }
            })
    }


    @SuppressLint("NewApi")
    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    }

    private fun isSystemPackage(applicationInfo: ApplicationInfo): Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}