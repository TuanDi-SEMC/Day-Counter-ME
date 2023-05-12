package com.diagonalley.daycounterme.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.base.BaseViewModel
import com.diagonalley.daycounterme.repository.FirebaseAuthRepository
import com.diagonalley.daycounterme.repository.impl.SignInCallback
import com.diagonalley.daycounterme.repository.impl.SignOutCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) :
    BaseViewModel() {
    fun setEvent(event: SplashEvent) {
        _event.value = event
    }

    private val _event = MutableLiveData<SplashEvent>()
    val event: LiveData<SplashEvent> get() = _event

    fun isSignedIn(signInCallback: SignInCallback) {
        val signed = firebaseAuthRepository.isSigned()
        if (!signed) {
            firebaseAuthRepository.signOut(callback = object : SignOutCallback {
                override fun onSignOutSuccess() {
                    signInCallback.onSignInFailure(null)
                }

                override fun onSignOutFailure(errorMsg: String?) {
                    signInCallback.onSignInFailure(errorMsg)
                }
            })
        } else {
            signInCallback.onSignInSuccess()
        }
    }
}

enum class SplashEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD
}