package com.redmadrobot.data.network

import com.redmadrobot.data.FakeDataGenerator
import com.redmadrobot.domain.model.AddFriendRequest
import com.redmadrobot.domain.model.UserProfile
import retrofit2.Response
import javax.inject.Inject

class FriendsApiMockImpl @Inject constructor() : FriendsApi {

    override suspend fun fetchUserFriendList(): List<UserProfile> {
        return FakeDataGenerator.fetchFakeFriendList()
    }

    override suspend fun deleteUserFriendFromList(id: String): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun addFriend(addFriendRequest: AddFriendRequest): Response<Unit> {
        return Response.success(Unit)
    }
}
