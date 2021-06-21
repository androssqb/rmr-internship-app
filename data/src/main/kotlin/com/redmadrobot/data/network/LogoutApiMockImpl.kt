package com.redmadrobot.data.network

import com.redmadrobot.domain.model.RefreshRequest
import com.redmadrobot.domain.model.Token
import retrofit2.Response
import javax.inject.Inject

@Suppress("MaxLineLength")
class LogoutApiMockImpl @Inject constructor() : LogoutApi {

    private companion object {
        private const val ACCESS =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        private const val REFRESH = "8feed535-5ca5-464e-862d-0de124800aa3"
    }

    override suspend fun logoutUser(): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun refreshToken(refreshRequest: RefreshRequest): Token {
        return Token(ACCESS, REFRESH)
    }
}
