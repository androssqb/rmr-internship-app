package com.redmadrobot.data.network

import com.redmadrobot.data.FakeDataGenerator
import com.redmadrobot.domain.model.Post
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class PostsApiMockImpl @Inject constructor() : PostsApi {

    private companion object {
        const val DELAY = 1_000L
    }

    override suspend fun fetchPosts(): List<Post> {
        delay(DELAY)
        return FakeDataGenerator.fetchFakePostList()
    }

    override suspend fun fetchFavoritePosts(): List<Post> {
        delay(DELAY)
        return FakeDataGenerator.fetchFakePostList().filter { post -> post.liked }
    }

    override suspend fun fetchUserPosts(): List<Post> {
        delay(DELAY)
        return FakeDataGenerator.fetchFakePostList().filter { post -> post.author.nickname == "andrey" }
    }

    override suspend fun like(id: String): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun removeLike(id: String): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun createPost(
        postText: RequestBody,
        image: MultipartBody.Part?,
        longitude: Double?,
        latitude: Double?,
    ): Post {
        return FakeDataGenerator.fetchFakePostList().first()
    }
}
