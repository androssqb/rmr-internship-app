package com.redmadrobot.data.network

import com.redmadrobot.domain.model.UserProfile
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface UserApi {

    @Multipart
    @PATCH("me")
    suspend fun updateUserProfile(
        @Part("first_name") firstName: String,
        @Part("last_name") lastName: String,
        @Part("nickname") nickname: String,
        @Part("avatar_file") avatar: String,
        @Part("birth_day") birthDay: String
    ): UserProfile

    @GET("me")
    suspend fun getUserProfile(): UserProfile
}
