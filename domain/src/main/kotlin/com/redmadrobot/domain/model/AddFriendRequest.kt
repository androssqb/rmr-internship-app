package com.redmadrobot.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddFriendRequest(
    @Json(name = "user_id")
    val userId: String
)
