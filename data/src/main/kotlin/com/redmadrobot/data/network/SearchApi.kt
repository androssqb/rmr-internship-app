package com.redmadrobot.data.network

import com.redmadrobot.domain.model.UserProfile
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search")
    suspend fun searchFriends(@Query("user") user: String): List<UserProfile>
}
