package com.redmadrobot.app.model

import androidx.annotation.DrawableRes

sealed class FriendItem

data class Friend(
    val id: String,
    val fullName: String,
    val nickname: String,
    val avatarUrl: String,
    @DrawableRes val actionIconId: Int
) : FriendItem()

data class FriendsFooter(val footer: String) : FriendItem()

data class NoFriends(val itemEmpty: ItemEmpty) : FriendItem()

data class FriendListError(val itemError: ItemEmpty) : FriendItem()
