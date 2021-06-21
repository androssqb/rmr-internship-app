package com.redmadrobot.data.network

import com.redmadrobot.domain.model.RefreshRequest
import com.redmadrobot.domain.model.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LogoutApi {

    @POST("auth/logout")
    suspend fun logoutUser(): Response<Unit>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): Token
}
