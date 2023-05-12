package com.diagonalley.daycounterme.ui.mywidget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.base.BaseViewModel
import com.diagonalley.daycounterme.ui.adapter.SignInView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyWidgetViewModel @Inject constructor() : BaseViewModel() {
    private val signInViewData = arrayListOf<SignInView>()

    private val _signInViews = MutableLiveData<List<SignInView>>()
    val signInViews: LiveData<List<SignInView>> get() = _signInViews

    init {

    }


    fun setEvent(event: SplashEvent) {
        _event.value = event
    }

    private val _event = MutableLiveData<SplashEvent>()
    val event: LiveData<SplashEvent> get() = _event
}

enum class SplashEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD
}

