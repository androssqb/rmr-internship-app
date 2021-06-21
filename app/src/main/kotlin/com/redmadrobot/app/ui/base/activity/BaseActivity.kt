package com.redmadrobot.app.ui.base.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.base.viewmodel.MainNavigateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.extensions.lifecycle.Event

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected open fun onEvent(event: Event) {
        when (event) {
            is MainNavigateEvent -> findNavController(R.id.main_nav_host_fragment).navigate(event.direction)
            is ShowMessageEvent -> showMessage(event)
        }
    }

    private fun showMessage(event: ShowMessageEvent) {
        val message = event.textMessage.ifEmpty { resources.getString(event.messageId) }
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}
