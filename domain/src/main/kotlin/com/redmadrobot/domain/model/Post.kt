package com.redmadrobot.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "id")
    val id: String,
    @Json(name = "text")
    val text: String?,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "author")
    val author: UserProfile,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "liked")
    val liked: Boolean,
)
