package com.diagonalley.daycounterme.ui.signin.phone

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.diagonalley.daycounterme.base.BaseViewModel
import com.diagonalley.daycounterme.data.model.UserInfo
import com.diagonalley.daycounterme.repository.FirebaseAuthRepository
import com.diagonalley.daycounterme.repository.FirestoreRepository
import com.diagonalley.daycounterme.repository.impl.FirestoreCallback
import com.diagonalley.daycounterme.repository.impl.SignInByPhoneCallback
import com.diagonalley.daycounterme.repository.impl.VerifyOTPCallback
import com.diagonalley.daycounterme.ui.signin.SignInType
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInByPhoneViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : BaseViewModel() {
    fun setEvent(event: SignInEvent) {
        _event.value = event
    }

    private val _event = MutableLiveData<SignInEvent>()
    val event: LiveData<SignInEvent> get() = _event

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
                        setEvent(SignInEvent.SIGN_IN_SUCCESS)
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
                    setEvent(SignInEvent.SIGN_IN_SUCCESS)
                }

                override fun onFailure(exception: Exception) {

                }
            })
    }

    fun verifyPhoneNumber(activity: FragmentActivity, phoneNumber: String) {
        viewModelScope.launch {
            firebaseAuthRepository.verifyPhoneNumber(activity,
                phoneNumber,
                callback = object : SignInByPhoneCallback {
                    override fun verifySuccess() {
                        Timber.d("verifySuccess")
                    }

                    override fun verifyFailure() {
                        Timber.d("verifyFailure")

                    }

                    override fun sentCode() {
                        Timber.d("sentCode")
                    }
                })
        }
    }

    fun verifyOTP(otp: String) {
        viewModelScope.launch {
            firebaseAuthRepository.verifyOTP(otp, object : VerifyOTPCallback {
                override fun verifySuccess(user: FirebaseUser) {
                    Timber.d("verifySuccess")
                    handleSignInResult(user, SignInType.Phone, "+84363211914")
                }

                override fun verifyFailure() {
                    Timber.d("verifyFailure")
                }
            })
        }
    }
}

enum class SignInEvent {
    SIGN_IN_SUCCESS
}

