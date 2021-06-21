package com.redmadrobot.data.repository

import com.redmadrobot.data.converters.ProfileMapper
import com.redmadrobot.data.database.FriendsDao
import com.redmadrobot.data.database.PostsDao
import com.redmadrobot.data.model.PostDbEntity
import com.redmadrobot.data.model.ProfileDbEntity
import com.redmadrobot.data.model.SearchResultProfile
import com.redmadrobot.data.network.FriendsApi
import com.redmadrobot.domain.constants.Constants.SEARCH_CACHE
import com.redmadrobot.domain.di.Origin
import com.redmadrobot.domain.model.AddFriendRequest
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.reactiveMutableMap
import com.redmadrobot.mapmemory.shared
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class FriendsRepository @Inject constructor(
    @Origin private val api: FriendsApi,
    private val friendsDao: FriendsDao,
    private val postsDao: PostsDao,
    private val profileMapper: ProfileMapper,
    preferencesProvider: PreferencesProvider,
    memory: MapMemory,
) {

    private val searchResultCache by memory.reactiveMutableMap<String, SearchResultProfile>().shared(SEARCH_CACHE)
    private var friendBackUp: ProfileDbEntity? by memory
    private var friendPostsBackUp: List<PostDbEntity>? by memory
    val friendList = friendsDao.getAllFriends(preferencesProvider.fetchUserId())

    fun fetchUserFriendList(): Flow<Unit> {
        return flow {
            val profileList = api.fetchUserFriendList()
            val friendList = profileMapper.mapProfileListToDatabaseEntity(profiles = profileList)
            postsDao.insertAllProfiles(friendList)
            emit(Unit)
        }
    }

    fun deleteFriend(friendId: String): Flow<Unit> {
        return flow {
            friendBackUp = friendsDao.getFriend(id = friendId)
            friendPostsBackUp = postsDao.fetchAllAuthorsPosts(authorId = friendId)
            friendsDao.deleteProfileAndPosts(profileId = friendId)
            val response = api.deleteUserFriendFromList(id = friendId)
            if (response.isSuccessful) {
                friendBackUp = null
                friendPostsBackUp = null
                emit(Unit)
            } else {
                throw HttpException(response)
            }
        }.catch { throwable: Throwable ->
            friendBackUp?.let { postsDao.insertProfile(profile = it) }
            friendPostsBackUp?.let { postsDao.insertAllPosts(postList = it) }
            friendBackUp = null
            friendPostsBackUp = null
            Timber.e(throwable, "An error occurred during the deleting friend process")
        }
    }

    fun addFriend(userId: String): Flow<Unit> {
        return flow {
            searchResultCache[userId] = searchResultCache.getValue(userId).copy(isFriend = true)
            searchResultCache[userId]?.let {
                val profile = ProfileDbEntity(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    nickname = it.nickname,
                    avatarUrl = it.avatarUrl,
                    birthDay = it.birthDay
                )
                postsDao.insertProfile(profile = profile)
            }
            val response = api.addFriend(AddFriendRequest(userId = userId))
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw HttpException(response)
            }
        }.catch { throwable: Throwable ->
            friendsDao.deleteProfile(id = userId)
            searchResultCache[userId] = searchResultCache.getValue(userId).copy(isFriend = false)
            Timber.e(throwable, "An error occurred during the adding friend process")
        }
    }
}
