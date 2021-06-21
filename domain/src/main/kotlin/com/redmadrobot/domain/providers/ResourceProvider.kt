package com.redmadrobot.domain.providers

import android.support.annotation.DimenRes
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes res: Int, vararg args: Any): String

    fun getQuantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any): String

    fun getDimension(@DimenRes res: Int): Float
}
