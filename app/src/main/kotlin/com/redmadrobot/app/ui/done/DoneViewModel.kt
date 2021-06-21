package com.redmadrobot.app.ui.done

import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import javax.inject.Inject

class DoneViewModel @Inject constructor() : BaseViewModel() {

    fun onDoneClicked() {
        val action = DoneFragmentDirections.actionDoneFragmentToBottomNavFragment()
        eventsQueue.offerEvent(Navigate(action))
    }
}
