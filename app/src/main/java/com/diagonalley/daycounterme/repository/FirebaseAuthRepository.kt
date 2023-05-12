package com.diagonalley.daycounterme.repository

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.diagonalley.daycounterme.repository.impl.SignInByPhoneCallback
import com.diagonalley.daycounterme.repository.impl.SignOutCallback
import com.diagonalley.daycounterme.repository.impl.VerifyOTPCallback
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential

interface FirebaseAuthRepository {
    fun getIntentSignInWithGoogle(): Intent
    fun handleGoogleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        signInSuccess: (FirebaseUser) -> Unit,
        signInFailure: () -> Unit,
    )

    fun saveUserInfoToLocal(userInfo: com.diagonalley.daycounterme.data.model.UserInfo)

    fun isSigned(): Boolean

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: VerifyOTPCallback,
    )

    fun verifyOTP(code: String, callback: VerifyOTPCallback)

    suspend fun verifyPhoneNumber(
        activity: FragmentActivity,
        phoneNumber: String,
        callback: SignInByPhoneCallback,
    ): Boolean

    fun signOut(callback: SignOutCallback)
}