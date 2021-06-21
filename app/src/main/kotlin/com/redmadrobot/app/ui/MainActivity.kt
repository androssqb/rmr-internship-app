package com.redmadrobot.app.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.activity.BaseActivity
import com.redmadrobot.app.utils.extension.dispatchApplyWindowInsetsToChild
import com.redmadrobot.extensions.lifecycle.observe
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        DI.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<FrameLayout>(R.id.activity_start_container_screens).dispatchApplyWindowInsetsToChild()

        observe(viewModel.eventsQueue, ::onEvent)
    }
}
