package com.diagonalley.daycounterme.ui.signin.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentSignInByPhoneBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.utils.clearAndStartActivity
import com.diagonalley.daycounterme.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignInByPhoneFragment : BaseFragment<FragmentSignInByPhoneBinding>() {
    @Inject
    lateinit var appConfig: AppConfig
    private val viewModel: SignInByPhoneViewModel by viewModels()


    override fun getLayoutResId(): Int = R.layout.fragment_sign_in_by_phone

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnVerify.setOnSingleClickListener {
                val phoneNumber = edtPhoneNumber.text.toString()
                viewModel.verifyPhoneNumber(requireActivity(), phoneNumber)
            }
            btnVerifyOTP.setOnSingleClickListener {
                viewModel.verifyOTP("123123")
            }
        }
        viewModel.apply {
            event.observe(viewLifecycleOwner) {
                when (it) {
                    SignInEvent.SIGN_IN_SUCCESS -> {
                        requireContext().clearAndStartActivity(MainActivity::class.java)
                    }
                }
            }
        }
    }
}

