package com.redmadrobot.data.network

import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.model.UserCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/registration")
    suspend fun registerNewUser(@Body credentials: UserCredentials): Token

    @POST("auth/login")
    suspend fun loginUser(@Body credentials: UserCredentials): Token
}
