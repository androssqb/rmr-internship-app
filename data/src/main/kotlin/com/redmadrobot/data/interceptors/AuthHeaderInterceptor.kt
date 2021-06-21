package com.redmadrobot.data.interceptors

import com.redmadrobot.domain.constants.Constants.HEADER_AUTHORIZATION
import com.redmadrobot.domain.providers.PreferencesProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthHeaderInterceptor @Inject constructor(private val prefs: PreferencesProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        prefs.fetchAccessToken().also { token ->
            requestBuilder.addHeader(HEADER_AUTHORIZATION, "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
