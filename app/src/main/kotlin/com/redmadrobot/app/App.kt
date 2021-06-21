package com.redmadrobot.app

import android.app.Application
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.utils.logging.PrettyLoggingTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        DI.init(this)
    }

    private fun initTimber() {
        Timber.plant(PrettyLoggingTree(this))
    }
}
