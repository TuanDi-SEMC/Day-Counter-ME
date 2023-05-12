package com.diagonalley.daycounterme.ui.profile.my

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentMyProfileBinding
import com.diagonalley.daycounterme.databinding.FragmentSetupProfileBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.utils.clearAndStartActivity
import com.diagonalley.daycounterme.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {
    @Inject
    lateinit var appConfig: AppConfig

    override fun getLayoutResId(): Int = R.layout.fragment_my_profile
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {}
    }
}

