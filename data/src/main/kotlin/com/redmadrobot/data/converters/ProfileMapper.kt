package com.redmadrobot.data.converters

import com.redmadrobot.data.model.ProfileDbEntity
import com.redmadrobot.domain.model.Post
import com.redmadrobot.domain.model.UserProfile
import javax.inject.Inject

class ProfileMapper @Inject constructor() {

    fun mapPostListToDatabaseEntity(postList: List<Post>): List<ProfileDbEntity> {
        return postList.map { post ->
            ProfileDbEntity(
                id = post.author.id,
                firstName = post.author.firstName,
                lastName = post.author.lastName,
                nickname = post.author.nickname,
                avatarUrl = post.author.avatarUrl,
                birthDay = post.author.birthDay
            )
        }
    }

    fun mapProfileListToDatabaseEntity(profiles: List<UserProfile>): List<ProfileDbEntity> {
        return profiles.map { profile ->
            ProfileDbEntity(
                id = profile.id,
                firstName = profile.firstName,
                lastName = profile.lastName,
                nickname = profile.nickname,
                avatarUrl = profile.avatarUrl,
                birthDay = profile.birthDay
            )
        }
    }

    fun mapProfileToDatabaseEntity(profile: UserProfile): ProfileDbEntity {
        return ProfileDbEntity(
            id = profile.id,
            firstName = profile.firstName,
            lastName = profile.lastName,
            nickname = profile.nickname,
            avatarUrl = profile.avatarUrl,
            birthDay = profile.birthDay
        )
    }
}
