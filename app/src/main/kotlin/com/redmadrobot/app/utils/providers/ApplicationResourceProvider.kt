package com.redmadrobot.app.utils.providers

import android.content.res.Resources
import com.redmadrobot.domain.providers.ResourceProvider
import javax.inject.Inject

class ApplicationResourceProvider @Inject constructor(private val resources: Resources) : ResourceProvider {

    override fun getString(res: Int, vararg args: Any): String {
        return resources.getString(res, *args)
    }

    override fun getQuantityString(res: Int, quantity: Int, vararg args: Any): String {
        return resources.getQuantityString(res, quantity, *args)
    }

    override fun getDimension(res: Int): Float {
        return resources.getDimension(res)
    }
}
