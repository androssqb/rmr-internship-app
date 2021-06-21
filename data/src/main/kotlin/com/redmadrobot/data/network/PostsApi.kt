package com.redmadrobot.data.network

import com.redmadrobot.domain.model.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @GET("feed")
    suspend fun fetchPosts(): List<Post>

    @GET("feed/favorite")
    suspend fun fetchFavoritePosts(): List<Post>

    @GET("me/posts")
    suspend fun fetchUserPosts(): List<Post>

    @POST("feed/{id}/like")
    suspend fun like(@Path("id") id: String): Response<Unit>

    @DELETE("feed/{id}/like")
    suspend fun removeLike(@Path("id") id: String): Response<Unit>

    @Multipart
    @POST("me/posts")
    suspend fun createPost(
        @Part("text") postText: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("lon") longitude: Double? = null,
        @Part("lat") latitude: Double? = null,
    ): Post
}
