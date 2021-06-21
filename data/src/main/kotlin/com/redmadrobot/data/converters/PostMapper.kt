package com.redmadrobot.data.converters

import com.redmadrobot.data.model.PostDbEntity
import com.redmadrobot.domain.model.Post
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapPostListToDatabaseEntity(postList: List<Post>): List<PostDbEntity> {
        return postList.map { post ->
            PostDbEntity(
                id = post.id,
                text = post.text,
                imageUrl = post.imageUrl,
                lon = post.lon,
                lat = post.lat,
                authorId = post.author.id,
                likes = post.likes,
                liked = post.liked
            )
        }
    }

    fun mapPostToDatabaseEntity(post: Post): PostDbEntity {
        return PostDbEntity(
            id = post.id,
            text = post.text,
            imageUrl = post.imageUrl,
            lon = post.lon,
            lat = post.lat,
            authorId = post.author.id,
            likes = post.likes,
            liked = post.liked
        )
    }
}
