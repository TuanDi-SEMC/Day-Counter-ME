package com.diagonalley.daycounterme.ui.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentSignInBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.ui.adapter.SignIn
import com.diagonalley.daycounterme.ui.adapter.SignInAdapter
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.utils.clearAndStartActivity
import com.diagonalley.daycounterme.utils.slideUp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    @Inject
    lateinit var appConfig: AppConfig
    private val viewModel: SignInViewModel by viewModels()
    private val signInViewAdapter by lazy {
        SignInAdapter {
            signIn(it.signIn)
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rcvSignIn.apply {
                adapter = signInViewAdapter
            }
            cardSignIn.slideUp()
        }
        viewModel.apply {
            signInViews.observe(viewLifecycleOwner) {
                signInViewAdapter.submitList(it)
            }
            event.observe(viewLifecycleOwner) {
                when (it) {
                    SplashEvent.CHECKED_VERSION -> {}
                    SplashEvent.AD_INITIALIZED -> {}
                    SplashEvent.LOAD_AD_FAILED -> {}
                    SplashEvent.LOAD_AD_SUCCESS -> {}
                    SplashEvent.SHOW_AD_FAILED -> {}
                    SplashEvent.DISMISS_AD -> {}
                }
            }
        }
    }

    private fun signIn(signIn: SignIn) {
        when (signIn) {
            SignIn.FACEBOOK -> {
                requireContext().clearAndStartActivity(MainActivity::class.java)
            }
            SignIn.GOOGLE -> {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSetupProfileFragment())
            }
            SignIn.PHONE_NUMBER -> {}
        }
    }
}

