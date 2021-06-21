package com.redmadrobot.app.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentSignInBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SignInViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentSignInBinding>()

    override val anchorViewId: Int
        get() = R.id.sign_in_button_sign_up

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.nextButtonEnabled, ::renderNextButton)
        observe(viewModel.isLoading, ::showProgressLoader)

        binding.signInToolbar.setNavigationOnClickListener {
            viewModel.onToolbarBackClicked()
        }

        binding.signInEditTextEmail.doAfterTextChanged { email ->
            viewModel.onEmailTextChanged(email = email.toString())
        }

        binding.signInEditTextPassword.doAfterTextChanged { password ->
            viewModel.onPasswordTextChanged(password = password.toString())
        }

        binding.signInButtonSignUp.setOnClickListener {
            viewModel.onSignUpClicked()
        }

        binding.signInButtonNext.setOnClickListener {
            viewModel.onNextClicked(
                email = binding.signInEditTextEmail.text.toString(),
                password = binding.signInEditTextPassword.text.toString()
            )
        }
    }

    private fun renderNextButton(isButtonEnabled: Boolean) {
        binding.signInButtonNext.isEnabled = isButtonEnabled
    }
}
