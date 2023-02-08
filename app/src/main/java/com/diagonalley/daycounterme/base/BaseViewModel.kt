package com.diagonalley.daycounterme.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

/**
 * Created by TuanTM on 9/7/20.
 */
abstract class BaseViewModel : ViewModel() {
    //region MARK: - public fields
    private val loading = ObservableBoolean(false)
    //region

    fun hideLoading() {
        loading.set(false)
    }

    fun showLoading() {
        loading.set(true)
    }

    open fun retry() {

    }
}