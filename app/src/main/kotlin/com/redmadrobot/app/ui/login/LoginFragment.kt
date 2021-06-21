package com.redmadrobot.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentLoginBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }
    private val binding: FragmentLoginBinding by viewBinding()

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isButtonsEnables, ::renderButtons)

        binding.loginSignInButton.setOnClickListener {
            viewModel.onSignInClicked()
        }

        binding.loginSignUpButton.setOnClickListener {
            viewModel.onSignUpClicked()
        }
    }

    private fun renderButtons(isButtonsEnabled: Boolean) {
        with(binding) {
            loginSignInButton.isEnabled = isButtonsEnabled
            loginSignUpButton.isEnabled = isButtonsEnabled
        }
    }
}
