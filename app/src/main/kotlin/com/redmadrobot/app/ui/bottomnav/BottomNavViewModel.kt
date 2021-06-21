package com.redmadrobot.app.ui.bottomnav

import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.utils.providers.UserFromPostNavigator
import com.redmadrobot.domain.di.BottomNavigationEvents
import com.redmadrobot.extensions.lifecycle.EventQueue
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomNavViewModel @Inject constructor(
    @BottomNavigationEvents val bottomNavigationEvents: EventQueue,
    private val userFromPostNavigator: UserFromPostNavigator,
) : BaseViewModel() {

    fun onDestinationChanged(@IdRes graphId: Int?) {
        viewModelScope.launch {
            if (graphId != null && graphId != R.id.post_graph) {
                userFromPostNavigator.setCurrentDestination(graphId)
            }
        }
    }
}
