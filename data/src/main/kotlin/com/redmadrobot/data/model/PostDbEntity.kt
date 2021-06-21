package com.redmadrobot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "text")
    val text: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "lon")
    val lon: Double?,
    @ColumnInfo(name = "lat")
    val lat: Double?,
    @ColumnInfo(name = "author_id")
    val authorId: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "liked")
    val liked: Boolean,
)
