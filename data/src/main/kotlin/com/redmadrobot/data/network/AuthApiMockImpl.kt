package com.redmadrobot.data.network

import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.model.UserCredentials
import javax.inject.Inject

@Suppress("MaxLineLength")
class AuthApiMockImpl @Inject constructor() : AuthApi {

    private companion object {
        private const val ACCESS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        private const val REFRESH = "8feed535-5ca5-464e-862d-0de124800aa3"
    }

    override suspend fun registerNewUser(credentials: UserCredentials): Token {
        return Token(ACCESS, REFRESH)
    }

    override suspend fun loginUser(credentials: UserCredentials): Token {
        return Token(ACCESS, REFRESH)
    }
}
