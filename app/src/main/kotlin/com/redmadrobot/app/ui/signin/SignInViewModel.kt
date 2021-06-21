package com.redmadrobot.app.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.NavigateUp
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.domain.usecases.AuthUseCase
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val auth: AuthUseCase,
    private val error: ErrorConverter
) : BaseViewModel() {

    private val signInViewState: MutableLiveData<SignInViewState> = MutableLiveData(createInitialState())
    val isLoading = signInViewState.mapDistinct { it.isLoading }
    val nextButtonEnabled = signInViewState.mapDistinct { it.email.isNotEmpty() && it.password.isNotEmpty() }
    private var state: SignInViewState by signInViewState

    fun onNextClicked(email: String, password: String) {
        auth.login(email = email, password = password)
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .onEach {
                val action = SignInFragmentDirections.actionSignInFragmentToDoneFragment()
                eventsQueue.offerEvent(Navigate(action))
            }
            .catch { throwable: Throwable ->
                val message = error.message(throwable)
                eventsQueue
                    .offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onEmailTextChanged(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordTextChanged(password: String) {
        state = state.copy(password = password)
    }

    fun onSignUpClicked() {
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFirstFragment()
        eventsQueue.offerEvent(Navigate(action))
    }

    fun onToolbarBackClicked() {
        eventsQueue.offerEvent(NavigateUp)
    }

    private fun createInitialState(): SignInViewState {
        return SignInViewState(
            email = "",
            password = "",
            isLoading = false
        )
    }

    data class SignInViewState(
        val email: String,
        val password: String,
        val isLoading: Boolean
    )
}
