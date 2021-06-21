package com.redmadrobot.app.ui.login

import android.os.Debug
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import com.scottyab.rootbeer.RootBeer
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val prefs: PreferencesProvider,
    private val rootBeer: RootBeer,
) : BaseViewModel() {

    private val loginViewState: MutableLiveData<LoginViewState> = MutableLiveData(createInitialState())
    val isButtonsEnables = loginViewState.mapDistinct { it.isButtonsEnabled }
    private var state: LoginViewState by loginViewState

    init {
        trustedEnvironmentCheck()
    }

    private fun trustedEnvironmentCheck() {
        if (rootBeer.isRooted || Debug.isDebuggerConnected()) {
            state = state.copy(isButtonsEnabled = false)
            eventsQueue.offerEvent(ShowMessageEvent(R.string.root_error))
        } else {
            navigateUser()
        }
    }

    private fun navigateUser() {
        if (prefs.fetchAccessToken().isNotEmpty()) {
            val action = LoginFragmentDirections.actionLoginFragmentToBottomNavFragment()
            eventsQueue.offerEvent(Navigate(action))
        }
    }

    fun onSignInClicked() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignInFragment()
        eventsQueue.offerEvent(Navigate(action))
    }

    fun onSignUpClicked() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFirstFragment()
        eventsQueue.offerEvent(Navigate(action))
    }

    private fun createInitialState(): LoginViewState {
        return LoginViewState(isButtonsEnabled = true)
    }

    data class LoginViewState(val isButtonsEnabled: Boolean)
}
