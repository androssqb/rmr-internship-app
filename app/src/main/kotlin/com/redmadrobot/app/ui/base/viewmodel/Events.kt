package com.redmadrobot.app.ui.base.viewmodel

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavDirections
import com.redmadrobot.app.R
import com.redmadrobot.extensions.lifecycle.Event

sealed class NavigationEvent : Event

data class Navigate(val direction: NavDirections) : NavigationEvent()

object NavigateUp : NavigationEvent()

object PopBackStackEvent : NavigationEvent()

data class MainNavigateEvent(val direction: NavDirections) : NavigationEvent()

data class BottomNavigationEvent(@IdRes val menuGraphId: Int) : NavigationEvent()

data class ShowMessageEvent(
    @StringRes val messageId: Int = R.string.unknown_error,
    val textMessage: String = "",
) : Event

object ShowDatePikerEvent : Event

data class SetSelectedDateEvent(val date: String) : Event
