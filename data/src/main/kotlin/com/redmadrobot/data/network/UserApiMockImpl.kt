package com.redmadrobot.data.network

import com.redmadrobot.domain.model.UserProfile
import javax.inject.Inject

class UserApiMockImpl @Inject constructor() : UserApi {

    override suspend fun updateUserProfile(
        firstName: String,
        lastName: String,
        nickname: String,
        avatar: String,
        birthDay: String,
    ): UserProfile {
        return UserProfile(
            id = "8feed535-5ca5-464e-862d-0de124800aa3",
            firstName = "Martin",
            lastName = "Fowler",
            nickname = "RefactorMan",
            avatarUrl = "https://interns2021.redmadrobot.com/user-avatars/1914558387.jpg",
            birthDay = "2021-12-21"
        )
    }

    override suspend fun getUserProfile(): UserProfile {
        return UserProfile(
            id = "8feed535-5ca5-464e-862d-0de124800aa3",
            firstName = "Martin",
            lastName = "Fowler",
            nickname = "RefactorMan",
            avatarUrl = "https://interns2021.redmadrobot.com/user-avatars/1914558387.jpg",
            birthDay = "2021-12-21"
        )
    }
}
