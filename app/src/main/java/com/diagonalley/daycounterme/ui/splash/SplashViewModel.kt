package com.diagonalley.daycounterme.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {
    fun setEvent(event: SplashEvent) {
        _event.value = event
    }

    private val _event = MutableLiveData<SplashEvent>()
    val event: LiveData<SplashEvent> get() = _event
}

enum class SplashEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD
}