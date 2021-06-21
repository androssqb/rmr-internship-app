package com.redmadrobot.app.ui

import android.os.Debug
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.MainNavigateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.ui.bottomnav.BottomNavFragmentDirections
import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.repository.LogoutRepository
import com.scottyab.rootbeer.RootBeer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val repository: LogoutRepository,
    private val rootBeer: RootBeer,
) : BaseViewModel() {

    init {
        trustedEnvironmentCheck()
    }

    private fun trustedEnvironmentCheck() {
        if (!rootBeer.isRooted && !Debug.isDebuggerConnected()) {
            logoutSubscription()
        }
    }

    private fun logoutSubscription() {
        repository.deAuthorization
            .onEach {
                when (it) {
                    DeAuthorizationType.LOGOUT_SUCCESS -> {
                        val action = BottomNavFragmentDirections.actionBottomNavFragmentToLoginFragment()
                        eventsQueue.offerEvent(MainNavigateEvent(action))
                    }

                    DeAuthorizationType.REFRESH_FAILED -> {
                        eventsQueue.offerEvent(ShowMessageEvent(R.string.session_finished_error))
                        val action = BottomNavFragmentDirections.actionBottomNavFragmentToLoginFragment()
                        eventsQueue.offerEvent(MainNavigateEvent(action))
                    }

                    DeAuthorizationType.NO_USER_DATA_FILE -> {
                        eventsQueue.offerEvent(ShowMessageEvent(R.string.unknown_error))
                        val action = BottomNavFragmentDirections.actionBottomNavFragmentToLoginFragment()
                        eventsQueue.offerEvent(MainNavigateEvent(action))
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
