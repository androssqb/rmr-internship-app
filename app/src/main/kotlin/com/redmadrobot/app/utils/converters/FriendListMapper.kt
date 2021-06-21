package com.redmadrobot.app.utils.converters

import com.redmadrobot.app.R
import com.redmadrobot.app.model.*
import com.redmadrobot.data.model.ProfileDbEntity
import com.redmadrobot.domain.providers.ResourceProvider
import javax.inject.Inject

class FriendListMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userDataConverter: UserDataConverter,
) {

    fun mapToFriendListWithFooter(profileList: List<ProfileDbEntity>): List<FriendItem> {
        val footer = resourceProvider.getString(R.string.profile_friends_find_more_button_title)
        return profileList.map { profile ->
            Friend(
                id = profile.id,
                fullName = userDataConverter
                    .convertToFullName(firstName = profile.firstName, lastName = profile.lastName),
                nickname = userDataConverter.convertToNickname(nickname = profile.nickname),
                avatarUrl = profile.avatarUrl.orEmpty(),
                actionIconId = R.drawable.icon_cross
            )
        } + listOf(FriendsFooter(footer))
    }

    fun friendListError(): List<FriendItem> {
        val errorItemContent = ItemEmpty(
            title = resourceProvider.getString(R.string.error_title_description),
            subtitle = resourceProvider.getString(R.string.error_subtitle_description),
            buttonText = resourceProvider.getString(R.string.update_button_title)
        )
        return listOf(FriendListError(errorItemContent))
    }

    fun mapNoFriends(): List<FriendItem> {
        val emptyItemContent = ItemEmpty(
            title = resourceProvider.getString(R.string.feed_empty_title_description),
            subtitle = resourceProvider.getString(R.string.feed_empty_subtitle_description),
            buttonText = resourceProvider.getString(R.string.feed_search_friends_button_title)
        )
        return listOf(NoFriends(emptyItemContent))
    }
}
