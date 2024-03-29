package com.diagonalley.daycounterme.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseViewModel
import com.diagonalley.daycounterme.data.model.UserInfo
import com.diagonalley.daycounterme.repository.FirebaseAuthRepository
import com.diagonalley.daycounterme.repository.FirestoreRepository
import com.diagonalley.daycounterme.repository.impl.FirestoreCallback
import com.diagonalley.daycounterme.ui.adapter.SignIn
import com.diagonalley.daycounterme.ui.adapter.SignInView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : BaseViewModel() {
    private val signInViewData = arrayListOf<SignInView>()

    private val _signInViews = MutableLiveData<List<SignInView>>()
    val signInViews: LiveData<List<SignInView>> get() = _signInViews

    init {
        signInViewData.add(
            SignInView(
                SignIn.FACEBOOK, R.drawable.ic_circle_facebook, R.string.continues_with_facebook
            )
        )
        signInViewData.add(
            SignInView(
                SignIn.GOOGLE, R.drawable.ic_circle_google, R.string.continues_with_google
            )
        )
        signInViewData.add(
            SignInView(
                SignIn.PHONE_NUMBER,
                R.drawable.ic_circle_facebook,
                R.string.continues_with_phone_number
            )
        )
        _signInViews.value = signInViewData
    }


    fun setEvent(event: SplashEvent) {
        _event.value = event
    }

    fun getSignInGoogleIntent() = firebaseAuthRepository.getIntentSignInWithGoogle()
    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        firebaseAuthRepository.handleGoogleSignInResult(completedTask = completedTask,
            signInSuccess = {
                handleSignInResult(it, SignInType.Google, completedTask.result.email)
            },
            signInFailure = {

            })
    }

    private val _event = MutableLiveData<SplashEvent>()
    val event: LiveData<SplashEvent> get() = _event

    private fun handleSignInResult(
        firebaseUser: FirebaseUser,
        signInType: SignInType,
        signInAccount: String?,
    ) {
        firestoreRepository.getUserInfoByUid(
            uid = firebaseUser.uid,
            callback = object : FirestoreCallback<UserInfo?> {
                override fun onSuccess(result: UserInfo?) {
                    if (result != null) {
                        firebaseAuthRepository.saveUserInfoToLocal(result)
                        setEvent(SplashEvent.SIGN_IN_SUCCESS)
                    } else {
                        val userInfo = UserInfo(
                            uid = firebaseUser.uid,
                            sign_in_type = signInType.value,
                            sign_in_account = signInAccount ?: ""
                        )
                        save(userInfo)
                    }
                }

                override fun onFailure(exception: Exception) {

                }
            })
    }

    private fun save(userInfo: UserInfo) {
        firestoreRepository.saveUserInfo(
            userInfo = userInfo,
            callback = object : FirestoreCallback<Void?> {
                override fun onSuccess(result: Void?) {
                    firebaseAuthRepository.saveUserInfoToLocal(userInfo)
                    setEvent(SplashEvent.SIGN_IN_SUCCESS)
                }

                override fun onFailure(exception: Exception) {

                }
            })
    }
}

enum class SignInType(val value: String) {
    Google("google"), Phone("phone")
}

enum class SplashEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD, SIGN_IN_SUCCESS
}

