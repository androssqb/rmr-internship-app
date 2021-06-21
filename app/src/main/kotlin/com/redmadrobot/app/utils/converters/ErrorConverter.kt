package com.redmadrobot.app.utils.converters

import com.redmadrobot.app.R
import com.redmadrobot.data.model.ServerException
import com.redmadrobot.domain.providers.ResourceProvider
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorConverter @Inject constructor(private val resourceProvider: ResourceProvider) {

    fun message(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> resourceProvider.getString(R.string.network_connection_error)
            is ServerException -> throwable.message ?: resourceProvider.getString(R.string.unknown_error)
            is HttpException -> resourceProvider.getString(R.string.unknown_error)
            else -> resourceProvider.getString(R.string.unknown_error)
        }
    }
}
