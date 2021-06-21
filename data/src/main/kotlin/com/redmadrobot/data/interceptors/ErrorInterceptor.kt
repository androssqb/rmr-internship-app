package com.redmadrobot.data.interceptors

import com.redmadrobot.data.model.ServerException
import com.redmadrobot.domain.model.Error
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor(private val moshi: Moshi) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request = request)
        if (!response.isSuccessful) {
            convertMessage(response.body?.string()).also { error ->
                throw ServerException(error)
            }
        }
        return response
    }

    private fun convertMessage(json: String?): Error? {
        val adapter = moshi.adapter(Error::class.java)
        return json?.let { adapter.fromJson(json) }
    }
}
