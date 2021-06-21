package com.redmadrobot.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Error(
    @Json(name = "message")
    val message: String,
    @Json(name = "code")
    val code: String
)
