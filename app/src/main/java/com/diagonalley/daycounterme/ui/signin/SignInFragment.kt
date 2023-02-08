package com.diagonalley.daycounterme.ui.signin

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentSignInBinding
import com.diagonalley.daycounterme.global.AppConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    @Inject
    lateinit var appConfig: AppConfig
    private val viewModel: SignInViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_sign_in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
            event.observe(this@SignInFragment) {
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
        binding.apply {

        }
    }

    fun signIn(signIn: SignIn) {
        when (signIn) {
            SignIn.FACEBOOK -> {}
            SignIn.GOOGLE -> {}
            SignIn.PHONE_NUMBER -> {}
        }
    }
}

enum class SignIn {
    FACEBOOK, GOOGLE, PHONE_NUMBER
}