package com.redmadrobot.data.interceptors

import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.repository.LogoutRepository
import com.redmadrobot.domain.constants.Constants.BEARER
import com.redmadrobot.domain.constants.Constants.HEADER_AUTHORIZATION
import com.redmadrobot.domain.providers.PreferencesProvider
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuthenticator @Inject constructor(
    private val prefs: PreferencesProvider,
    private val repository: LogoutRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentAccessToken = prefs.fetchAccessToken()
        val requestAccessToken = response.request.header(HEADER_AUTHORIZATION)?.removePrefix(BEARER)

        return if (currentAccessToken == requestAccessToken) {
            val newAccessToken = refreshTokenSynchronously()
            newAccessToken?.let { buildRequestWithNewAccessToken(response, newAccessToken) }
        } else {
            buildRequestWithNewAccessToken(response, currentAccessToken)
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun refreshTokenSynchronously(): String? {
        return runBlocking {
            try {
                val refreshToken = prefs.fetchRefreshToken()
                val token = repository.refreshToken(refreshToken).single()
                prefs.saveToken(token)
                token.accessToken
            } catch (exception: Exception) {
                Timber.e(exception, "An error occurred while token updating")
                repository.clearData(DeAuthorizationType.REFRESH_FAILED)
                null
            }
        }
    }

    private fun buildRequestWithNewAccessToken(response: Response, newAccessToken: String): Request {
        return response.request
            .newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $newAccessToken")
            .build()
    }
}
