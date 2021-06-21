package com.redmadrobot.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshRequest(
    @Json(name = "token")
    val token: String
)
