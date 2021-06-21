package com.redmadrobot.data.repository

import com.redmadrobot.data.network.AuthApi
import com.redmadrobot.domain.di.Origin
import com.redmadrobot.domain.model.UserCredentials
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.domain.providers.PreferencesProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @Origin private val api: AuthApi,
    private val prefs: PreferencesProvider,
    private val dispatcher: DispatcherProvider
) {

    fun loginUser(email: String, password: String): Flow<Unit> {
        return flow {
            val credentials = UserCredentials(email = email, password = password)
            val token = api.loginUser(credentials)
            prefs.saveToken(token = token)
            emit(Unit)
        }.flowOn(dispatcher.io)
    }

    fun registerNewUser(email: String, password: String): Flow<Unit> {
        return flow {
            val credentials = UserCredentials(email = email, password = password)
            val token = api.registerNewUser(credentials)
            prefs.saveToken(token = token)
            emit(Unit)
        }.flowOn(dispatcher.io)
    }
}
