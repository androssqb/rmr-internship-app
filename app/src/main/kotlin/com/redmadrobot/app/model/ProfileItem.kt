package com.redmadrobot.app.model

import androidx.annotation.DrawableRes

sealed class ProfileItem

data class Profile(
    val id: String,
    val fullName: String,
    val nickname: String,
    val avatarUrl: String,
    @DrawableRes val actionIconId: Int,
) : ProfileItem()

data class ProfileListError(val itemError: ItemEmpty) : ProfileItem()
