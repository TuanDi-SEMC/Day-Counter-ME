package com.diagonalley.daycounterme.repository.impl

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.data.model.UserInfo
import com.diagonalley.daycounterme.global.SharedPreps
import com.diagonalley.daycounterme.repository.FirebaseAuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreps: SharedPreps,
) : FirebaseAuthRepository {
    private lateinit var mVerificationId: String
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail()
            .build()
    }

    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(context, gso)
    }

    override fun getIntentSignInWithGoogle(): Intent = mGoogleSignInClient.signInIntent

    override fun handleGoogleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        signInSuccess: (FirebaseUser) -> Unit,
        signInFailure: () -> Unit,
    ) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val name = account.displayName
            val email = account.email
            Toast.makeText(context, "Welcome, $name ($email)", Toast.LENGTH_SHORT).show()
            Timber.d("firebaseAuthWithGoogle:" + account.id)
            val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user: FirebaseUser = firebaseAuth.currentUser as FirebaseUser
                    Toast.makeText(
                        context, "Authentication success.", Toast.LENGTH_SHORT
                    ).show()
                    signInSuccess.invoke(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication Failed.", Toast.LENGTH_SHORT
                    ).show()
                    signInFailure.invoke()
                }
            }
        } catch (e: ApiException) {
            // Sign in failed, display an error message to the user
            Toast.makeText(context, "Sign in failed, please try again", Toast.LENGTH_SHORT).show()
            Timber.d("signInResult:failed code=" + e.statusCode)
        }
    }

    override fun saveUserInfoToLocal(userInfo: UserInfo) {
        Timber.d("Save user into shared preference")
        sharedPreps.userInfo = userInfo
    }

    override fun isSigned(): Boolean {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid
        val userInfo = sharedPreps.userInfo
        return uid != null && userInfo != null && uid == userInfo.uid
    }

    override fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: VerifyOTPCallback,
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener() { task ->
            val user = task.result?.user
            if (task.isSuccessful && user != null) {
                // Sign in success, update UI with the signed-in user's information
                Timber.d("signInWithCredential:success")
                callback.verifySuccess(user)
            } else {
                // Sign in failed, display a message and update the UI
                Timber.w("signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }

                callback.verifyFailure()
                // Update UI
            }
        }
    }

    override fun verifyOTP(code: String, callback: VerifyOTPCallback) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
        signInWithPhoneAuthCredential(credential, callback)
    }

    override suspend fun verifyPhoneNumber(
        activity: FragmentActivity,
        phoneNumber: String,
        callback: SignInByPhoneCallback,
    ): Boolean {
        val resultCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                callback.verifySuccess()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Timber.w("onVerificationFailed", e);

                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                    }
                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                    }
                }
                callback.verifyFailure()
                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Timber.d("onCodeSent: $verificationId");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
//                mResendToken = token;
                callback.sentCode()
            }
        }
        val options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity).setCallbacks(resultCallback)
            .build()

        return suspendCoroutine {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override fun signOut(callback: SignOutCallback) {
        sharedPreps.userInfo = null
        firebaseAuth.addAuthStateListener {
            if (it.currentUser == null) {
                callback.onSignOutSuccess()
            } else {
                callback.onSignOutFailure()
            }
        }
        firebaseAuth.signOut()
    }
}

interface VerifyOTPCallback {
    fun verifySuccess(user: FirebaseUser)
    fun verifyFailure()
}

interface SignInByPhoneCallback {
    fun verifySuccess()
    fun verifyFailure()
    fun sentCode()
}

interface SignInCallback {
    fun onSignInSuccess()
    fun onSignInFailure(errorMsg: String?)
}

interface SignOutCallback {
    fun onSignOutSuccess()
    fun onSignOutFailure(errorMsg: String? = null)
}