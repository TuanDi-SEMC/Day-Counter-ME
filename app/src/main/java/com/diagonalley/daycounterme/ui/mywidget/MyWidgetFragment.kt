package com.diagonalley.daycounterme.ui.mywidget

import android.os.Bundle
import android.view.View
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentMyWidgetBinding
import com.diagonalley.daycounterme.databinding.FragmentSignInBinding
import com.diagonalley.daycounterme.global.AppConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyWidgetFragment : BaseFragment<FragmentMyWidgetBinding>() {
    @Inject
    lateinit var appConfig: AppConfig

    override fun getLayoutResId(): Int = R.layout.fragment_my_widget
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
        }
    }
}

