package com.redmadrobot.data.model

data class SearchResultProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val nickname: String?,
    val avatarUrl: String?,
    val birthDay: String,
    val isFriend: Boolean,
)
