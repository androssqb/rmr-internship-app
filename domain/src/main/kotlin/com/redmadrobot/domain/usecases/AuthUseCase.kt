package com.redmadrobot.domain.usecases

import com.redmadrobot.domain.model.UserCredentials
import com.redmadrobot.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun login(email: String, password: String): Flow<Unit>

    fun checkIn(credentials: UserCredentials, profile: UserProfile): Flow<Unit>
}
