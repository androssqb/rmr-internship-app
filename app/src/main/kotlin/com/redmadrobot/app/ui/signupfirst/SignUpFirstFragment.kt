package com.redmadrobot.app.ui.signupfirst

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentSignUpFirstBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class SignUpFirstFragment : BaseFragment(R.layout.fragment_sign_up_first) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SignUpFirstViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentSignUpFirstBinding>()

    override val anchorViewId: Int
        get() = R.id.sign_up_first_button_sign_in

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.nextButtonEnabled, ::renderNextButton)

        binding.signUpFirstToolbar.setNavigationOnClickListener {
            viewModel.onToolbarBackClicked()
        }

        binding.signUpFirstEditTextEmail.doAfterTextChanged { email ->
            viewModel.onEmailTextChanged(email = email.toString())
        }

        binding.signUpFirstEditTextPassword.doAfterTextChanged { password ->
            viewModel.onPasswordTextChanged(password = password.toString())
        }

        binding.signUpFirstButtonNext.setOnClickListener {
            viewModel.onNextClicked(
                email = binding.signUpFirstEditTextEmail.text.toString(),
                password = binding.signUpFirstEditTextPassword.text.toString()
            )
        }

        binding.signUpFirstButtonSignIn.setOnClickListener {
            viewModel.onSignInClicked()
        }
    }

    private fun renderNextButton(isButtonEnabled: Boolean) {
        binding.signUpFirstButtonNext.isEnabled = isButtonEnabled
    }
}
