package com.diagonalley.daycounterme.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.base.BaseViewModel
import com.diagonalley.daycounterme.global.SharedPreps
import com.diagonalley.daycounterme.manager.MyBiometricManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreps: SharedPreps,
    private val biometricManager: MyBiometricManager,
) : BaseViewModel() {

    private val _settingViews = MutableLiveData<Boolean>()
    val settingViews: LiveData<Boolean> get() = _settingViews

    init {
        _settingViews.value = sharedPreps.isBiometricEnabled
    }


    fun setEvent(event: SplashEvent) {
        _event.value = event
    }

    fun onEnableBiometric(enable: Boolean) {
        biometricManager.isBiometricEnable { isEnabled, message, notSetup, intent ->

        }
        sharedPreps.isBiometricEnabled = enable
    }

    private val _event = MutableLiveData<SplashEvent>()
    val event: LiveData<SplashEvent> get() = _event
}

enum class SplashEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD
}

