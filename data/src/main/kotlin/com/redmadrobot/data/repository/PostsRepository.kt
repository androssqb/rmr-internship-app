package com.redmadrobot.data.repository

import com.redmadrobot.data.converters.PostMapper
import com.redmadrobot.data.converters.ProfileMapper
import com.redmadrobot.data.database.FavoritePostsDao
import com.redmadrobot.data.database.PostsDao
import com.redmadrobot.data.network.PostsApi
import com.redmadrobot.domain.di.Origin
import com.redmadrobot.domain.model.Post
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.reactiveMutableMap
import com.redmadrobot.mapmemory.valuesFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

@Suppress("LongParameterList")
class PostsRepository @Inject constructor(
    @Origin private val api: PostsApi,
    private val postsDao: PostsDao,
    favoritePostsDao: FavoritePostsDao,
    private val postMapper: PostMapper,
    private val profileMapper: ProfileMapper,
    memory: MapMemory,
) {

    val postList = postsDao.fetchPostAndProfileList()
    val favoriteList = favoritePostsDao.fetchFavoritePostAndProfileList()

    private val userPostsCache by memory.reactiveMutableMap<String, Post>()
    val userPosts: Flow<Collection<Post>> = userPostsCache.valuesFlow

    fun fetchPosts(): Flow<Unit> {
        return flow {
            val posts = api.fetchPosts()
            val postList = postMapper.mapPostListToDatabaseEntity(postList = posts)
            val authorsList = profileMapper.mapPostListToDatabaseEntity(postList = posts)
            postsDao.insertPostAndProfileList(postList = postList, profiles = authorsList)
            emit(Unit)
        }
    }

    fun fetchFavoritePosts(): Flow<Unit> {
        return flow {
            val favorite = api.fetchFavoritePosts()
            val favoriteList = postMapper.mapPostListToDatabaseEntity(postList = favorite)
            val authorsList = profileMapper.mapPostListToDatabaseEntity(postList = favorite)
            postsDao.insertPostAndProfileList(postList = favoriteList, profiles = authorsList)
            emit(Unit)
        }
    }

    fun fetchUsersPostsFromServer(): Flow<Unit> {
        return flow {
            val userPosts = api.fetchUserPosts()
            userPostsCache.replaceAll(userPosts.associateBy { it.id })
            emit(Unit)
        }
    }

    fun changeLike(id: String): Flow<Unit> {
        return flow {
            val clickedPost = postsDao.fetchPostAndProfile(id = id)
            postsDao.changePostLike(id = id, liked = !clickedPost.post.liked)
            val response = if (!clickedPost.post.liked) api.like(id) else api.removeLike(id)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw HttpException(response)
            }
        }.catch {
            val clickedPost = postsDao.fetchPostAndProfile(id = id)
            postsDao.changePostLike(id = id, liked = !clickedPost.post.liked)
        }
    }

    fun createPost(
        postText: RequestBody,
        image: MultipartBody.Part?,
        longitude: Double?,
        latitude: Double?,
    ): Flow<Unit> {
        return flow {
            val post = api.createPost(postText = postText, image = image, longitude = longitude, latitude = latitude)
            val createdPost = postMapper.mapPostToDatabaseEntity(post = post)
            val author = profileMapper.mapProfileToDatabaseEntity(profile = post.author)
            postsDao.insertPostAndProfile(post = createdPost, profile = author)
            userPostsCache.replace(post.id, post)
            emit(Unit)
        }
    }
}
