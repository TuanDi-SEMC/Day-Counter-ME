package com.diagonalley.daycounterme.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MyBiometricManager @Inject constructor(@ApplicationContext context: Context) {
    private val biometricManager = BiometricManager.from(context)

    fun isBiometricEnable(
        onResult: (isEnabled: Boolean, message: String, notSetup: Boolean?, intent: Intent?) -> Unit,
    ) {
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                onResult.invoke(true, "App can authenticate using biometrics.", null, null)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onResult.invoke(
                    false,
                    "No biometric features available on this device.",
                    null,
                    null
                )
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onResult.invoke(false, "Biometric features are currently unavailable.", null, null)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onResult.invoke(
                    false,
                    "Biometric feature none enrolled",
                    true,
                    getIntentForBiometric()
                )
            }
            else -> {
                onResult.invoke(false, "Biometric error", null, null)
            }
        }
    }

    fun authenticateByBiometric(
        activity: FragmentActivity,
        listener: BiometricAuthenticationListener,
    ) {
        val biometricPrompt =
            BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener.onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    listener.onFailure()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    listener.onFailure()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun getIntentForBiometric(): Intent {
        // Prompts the user to create credentials that your app accepts.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Intent(Settings.ACTION_FINGERPRINT_ENROLL)
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
    }
}

interface BiometricAuthenticationListener {
    fun onSuccess()
    fun onFailure()
}