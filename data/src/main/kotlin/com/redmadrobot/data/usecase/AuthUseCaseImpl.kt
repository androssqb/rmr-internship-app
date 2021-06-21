package com.redmadrobot.data.usecase

import com.redmadrobot.data.repository.AuthRepository
import com.redmadrobot.data.repository.UserRepository
import com.redmadrobot.domain.model.UserCredentials
import com.redmadrobot.domain.model.UserProfile
import com.redmadrobot.domain.usecases.AuthUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : AuthUseCase {

    override fun login(email: String, password: String): Flow<Unit> {
        val login = authRepository.loginUser(email = email, password = password)
        val getUser = userRepository.fetchUserProfileFromServer()
        return login.flatMapLatest { getUser }
    }

    override fun checkIn(credentials: UserCredentials, profile: UserProfile): Flow<Unit> {
        val checkIn = authRepository.registerNewUser(email = credentials.email, password = credentials.password)
        val updateUser = userRepository.updateUserProfile(
            nickname = profile.nickname,
            firstName = profile.firstName,
            secondName = profile.lastName,
            birthDate = profile.birthDay
        )
        return checkIn.flatMapLatest { updateUser }
    }
}
