package com.redmadrobot.app.di

import android.content.Context
import com.redmadrobot.app.di.components.AppComponent
import com.redmadrobot.app.di.components.DaggerAppComponent

object DI {

    lateinit var appComponent: AppComponent

    fun init(context: Context) {
        appComponent = DaggerAppComponent
            .builder()
            .context(context = context)
            .build()
    }
}
