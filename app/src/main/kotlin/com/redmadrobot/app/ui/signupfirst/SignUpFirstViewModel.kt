package com.redmadrobot.app.ui.signupfirst

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.model.FieldState
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.NavigateUp
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.FieldValidator
import com.redmadrobot.app.utils.Validators
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignUpFirstViewModel @Inject constructor(private val validators: Validators) : BaseViewModel() {

    private val singUpFirstViewState: MutableLiveData<SignUpFirstViewState> = MutableLiveData(createInitialState())
    val nextButtonEnabled = singUpFirstViewState.mapDistinct { it.email.isValid && it.password.isValid }
    private var state: SignUpFirstViewState by singUpFirstViewState

    init {
        with(validators) {
            emailValidator.collect(R.string.user_credentials_email_error) { state.copy(email = it) }
            passwordValidator.collect(R.string.user_credentials_password_error) { state.copy(password = it) }
        }
    }

    private inline fun FieldValidator.collect(
        @StringRes errorRes: Int,
        crossinline getState: (FieldState) -> SignUpFirstViewState
    ) {
        observeFieldState()
            .onEach { fieldState ->
                state = getState(fieldState)
                if (!fieldState.isValid && fieldState.value.isNotEmpty()) {
                    eventsQueue.offerEvent(ShowMessageEvent(errorRes))
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEmailTextChanged(email: String) {
        validators.emailValidator.setText(text = email)
    }

    fun onPasswordTextChanged(password: String) {
        validators.passwordValidator.setText(text = password)
    }

    fun onNextClicked(email: String, password: String) {
        val isUserCredentialsValid = with(validators) {
            emailValidator.validateField(text = email) && passwordValidator.validateField(text = password)
        }
        if (isUserCredentialsValid) {
            val action =
                SignUpFirstFragmentDirections.actionSignUpFirstFragmentToSignUpSecondFragment(email, password)
            eventsQueue.offerEvent(Navigate(action))
        }
    }

    fun onSignInClicked() {
        val action = SignUpFirstFragmentDirections.actionSignUpFirstFragmentToSignInFragment()
        eventsQueue.offerEvent(Navigate(action))
    }

    fun onToolbarBackClicked() {
        eventsQueue.offerEvent(NavigateUp)
    }

    private fun createInitialState(): SignUpFirstViewState {
        return SignUpFirstViewState(
            email = FieldState(value = "", isValid = false),
            password = FieldState(value = "", isValid = false)
        )
    }

    data class SignUpFirstViewState(
        val email: FieldState,
        val password: FieldState
    )
}
