package com.redmadrobot.data.repository

import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.network.LogoutApi
import com.redmadrobot.data.security.Encrypter
import com.redmadrobot.domain.di.Origin
import com.redmadrobot.domain.model.RefreshRequest
import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.mapmemory.MapMemory
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutRepository @Inject constructor(
    @Origin private val api: LogoutApi,
    private val prefs: PreferencesProvider,
    private val dispatcher: DispatcherProvider,
    private val memory: MapMemory,
    private val encrypter: Encrypter
) {

    private val _deAuthorization = MutableSharedFlow<DeAuthorizationType>()
    val deAuthorization: SharedFlow<DeAuthorizationType> = _deAuthorization.asSharedFlow()

    fun refreshToken(refreshToken: String): Flow<Token> {
        return flow {
            val refreshRequest = RefreshRequest(token = refreshToken)
            val token = api.refreshToken(refreshRequest = refreshRequest)
            emit(token)
        }.flowOn(dispatcher.io)
    }

    fun logout(): Flow<Unit> {
        return flow {
            val response = api.logoutUser()
            clearData(DeAuthorizationType.LOGOUT_SUCCESS)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw HttpException(response)
            }
        }
            .flowOn(dispatcher.io)
            .catch { throwable: Throwable ->
                Timber.e(throwable, "An error occurred during the logout process")
            }
    }

    suspend fun clearData(logoutType: DeAuthorizationType) {
        prefs.clearToken()
        memory.clear()
        encrypter.deleteFile()
        _deAuthorization.emit(logoutType)
    }
}
