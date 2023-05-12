package com.diagonalley.daycounterme.ui.profile.setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentSetupProfileBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.utils.clearAndStartActivity
import com.diagonalley.daycounterme.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>() {
    @Inject
    lateinit var appConfig: AppConfig

    override fun getLayoutResId(): Int = R.layout.fragment_setup_profile
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnContinue.setOnSingleClickListener {
                requireContext().clearAndStartActivity(MainActivity::class.java)
            }
        }
    }
}

