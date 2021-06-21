package com.redmadrobot.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserCredentials(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)
