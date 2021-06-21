package com.redmadrobot.app.ui.base.viewmodel

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import com.redmadrobot.extensions.lifecycle.EventQueue

open class BaseViewModel : ViewModel() {

    /**
     * LiveData для событий, которые должны быть обработаны один раз
     * Например: показы диалогов, снэкбаров с ошибками
     */
    val eventsQueue = EventQueue()

    protected fun EventQueue.navigate(@IdRes menuGraphId: Int) {
        offerEvent(BottomNavigationEvent(menuGraphId))
    }
}
