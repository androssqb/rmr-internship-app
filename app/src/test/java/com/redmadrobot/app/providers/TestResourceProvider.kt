package com.redmadrobot.app.providers

import com.redmadrobot.domain.providers.ResourceProvider

class TestResourceProvider : ResourceProvider {

    override fun getString(res: Int, vararg args: Any): String = "$res"

    override fun getQuantityString(res: Int, quantity: Int, vararg args: Any): String = "$quantity $res"

    override fun getDimension(res: Int): Float = res.toFloat()
}
