package com.redmadrobot.data.repository

import com.redmadrobot.data.database.FriendsDao
import com.redmadrobot.data.model.SearchResultProfile
import com.redmadrobot.data.network.SearchApi
import com.redmadrobot.domain.constants.Constants.SEARCH_CACHE
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.reactiveMutableMap
import com.redmadrobot.mapmemory.shared
import com.redmadrobot.mapmemory.valuesFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val api: SearchApi,
    private val friendsDao: FriendsDao,
    memory: MapMemory,
) {

    private val searchResultCache by memory.reactiveMutableMap<String, SearchResultProfile>().shared(SEARCH_CACHE)
    val searchResult: Flow<Collection<SearchResultProfile>> = searchResultCache.valuesFlow

    fun searchFriends(user: String): Flow<Unit> {
        return flow {
            val profileList = api.searchFriends(user).map { profile ->
                SearchResultProfile(
                    id = profile.id,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    nickname = profile.nickname,
                    avatarUrl = profile.avatarUrl,
                    birthDay = profile.birthDay,
                    isFriend = friendsDao.isAlreadyFriend(profile.id)
                )
            }
            searchResultCache.replaceAll(profileList.associateBy { it.id })
            emit(Unit)
        }
    }

    fun clearCache() {
        searchResultCache.clear()
    }
}
