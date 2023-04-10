package com.diagonalley.daycounterme.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentSettingBinding
import com.diagonalley.daycounterme.global.AppConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    @Inject
    lateinit var appConfig: AppConfig
    private val viewModel: SettingViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_setting
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            swBiometric.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEnableBiometric(isChecked)
            }
            viewModel.settingViews.observe(viewLifecycleOwner) { isEnabled ->
                swBiometric.isChecked = isEnabled
            }
        }
    }
}

