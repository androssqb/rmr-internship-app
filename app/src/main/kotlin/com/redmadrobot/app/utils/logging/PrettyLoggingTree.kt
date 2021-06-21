package com.redmadrobot.app.utils.logging

import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.redmadrobot.app.R
import timber.log.Timber

@Suppress("MagicNumber")
class PrettyLoggingTree(context: Context) : Timber.Tree() {

    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag(context.getString(R.string.app_name))
            .showThreadInfo(false)
            .methodCount(2)
            .methodOffset(7)
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Logger.log(priority, tag, message, t)
    }
}
