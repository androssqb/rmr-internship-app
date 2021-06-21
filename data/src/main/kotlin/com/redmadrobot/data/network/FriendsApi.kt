package com.redmadrobot.data.network

import com.redmadrobot.domain.model.AddFriendRequest
import com.redmadrobot.domain.model.UserProfile
import retrofit2.Response
import retrofit2.http.*

interface FriendsApi {

    @GET("me/friends")
    suspend fun fetchUserFriendList(): List<UserProfile>

    @DELETE("me/friends/{id}")
    suspend fun deleteUserFriendFromList(@Path("id") id: String): Response<Unit>

    @POST("me/friends")
    suspend fun addFriend(@Body addFriendRequest: AddFriendRequest): Response<Unit>
}
