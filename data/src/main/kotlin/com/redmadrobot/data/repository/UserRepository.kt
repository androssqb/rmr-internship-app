package com.redmadrobot.data.repository

import com.redmadrobot.data.network.UserApi
import com.redmadrobot.data.security.Encrypter
import com.redmadrobot.domain.di.Origin
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.sharedFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @Origin private val api: UserApi,
    memory: MapMemory,
    private val dispatcher: DispatcherProvider,
    private val encrypter: Encrypter,
    private val preferencesProvider: PreferencesProvider
) {

    private val fileCache by memory.sharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val fileName: SharedFlow<String> = fileCache

    fun updateUserProfile(
        nickname: String?,
        firstName: String,
        secondName: String,
        birthDate: String,
        avatarUrl: String? = null,
    ): Flow<Unit> {
        return flow {
            val profile = api.updateUserProfile(
                firstName = firstName,
                lastName = secondName,
                nickname = nickname.orEmpty(),
                avatar = avatarUrl.orEmpty(),
                birthDay = birthDate
            )
            encrypter.encryptProfile(profile)
            fileCache.emit(encrypter.fileName)
            emit(Unit)
        }.flowOn(dispatcher.io)
    }

    fun fetchUserProfileFromServer(): Flow<Unit> {
        return flow {
            val profile = api.getUserProfile()
            encrypter.encryptProfile(profile)
            preferencesProvider.saveUserId(profile.id)
            fileCache.emit(encrypter.fileName)
            emit(Unit)
        }.flowOn(dispatcher.io)
    }
}
