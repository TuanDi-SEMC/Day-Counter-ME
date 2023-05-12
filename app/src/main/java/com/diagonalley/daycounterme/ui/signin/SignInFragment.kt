package com.diagonalley.daycounterme.ui.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
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
    private val signInGoogleLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val data = it.data
            if (it.resultCode == Activity.RESULT_OK && data != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                viewModel.handleGoogleSignInResult(task)
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
                    SplashEvent.SIGN_IN_SUCCESS -> {
                        MainActivity.clearAndStart(requireContext())
                    }
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
                signInGoogleLauncher.launch(viewModel.getSignInGoogleIntent())
            }
            SignIn.PHONE_NUMBER -> {
                findNavController().navigate(R.id.signInByPhoneFragment)
            }
        }
    }
}

