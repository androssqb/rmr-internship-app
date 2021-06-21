package com.redmadrobot.app.utils.converters

import androidx.annotation.StringRes
import com.redmadrobot.app.R
import com.redmadrobot.app.model.*
import com.redmadrobot.data.model.relations.PostAndProfile
import com.redmadrobot.domain.model.Post
import com.redmadrobot.domain.providers.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedListMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userDataConverter: UserDataConverter,
    private val postDataConverter: PostDataConverter,
) {

    fun mapPostListMapMemory(postList: Collection<Post>): List<FeedItem> {
        return postList.map { post ->
            FeedPost(
                id = post.id,
                text = post.text.orEmpty(),
                imageUrl = post.imageUrl.orEmpty(),
                geo = postDataConverter.convertCoordinatesToAddress(post.lat, post.lon),
                authorNick = userDataConverter.convertToNickname(post.author.nickname),
                liked = post.liked
            )
        }
    }

    fun mapPostAndProfileListWithHeader(
        @StringRes headerId: Int,
        postAndProfileList: List<PostAndProfile>,
    ): List<FeedItem> {
        val header = resourceProvider.getString(headerId)
        return listOf(FeedHeader(header)) + mapPostAndProfileList(postAndProfileList)
    }

    fun mapPostAndProfileList(postAndProfileList: List<PostAndProfile>): List<FeedItem> {
        return postAndProfileList.map { postAndProfile ->
            FeedPost(
                id = postAndProfile.post.id,
                text = postAndProfile.post.text.orEmpty(),
                imageUrl = postAndProfile.post.imageUrl.orEmpty(),
                geo = postDataConverter.convertCoordinatesToAddress(postAndProfile.post.lat, postAndProfile.post.lon),
                authorNick = userDataConverter.convertToNickname(postAndProfile.profile.nickname),
                liked = postAndProfile.post.liked
            )
        }
    }

    fun mapNoFriends(): List<FeedItem> {
        val emptyItemContent = ItemEmpty(
            imageId = R.drawable.image_feed_empty,
            title = resourceProvider.getString(R.string.feed_empty_title_description),
            subtitle = resourceProvider.getString(R.string.feed_empty_subtitle_description),
            buttonText = resourceProvider.getString(R.string.feed_search_friends_button_title)
        )
        return listOf(FeedEmpty(emptyItemContent))
    }

    fun postListError(): List<FeedItem> {
        val errorItemContent = ItemEmpty(
            imageId = R.drawable.image_feed_error,
            title = resourceProvider.getString(R.string.error_title_description),
            subtitle = resourceProvider.getString(R.string.error_subtitle_description),
            buttonText = resourceProvider.getString(R.string.update_button_title)
        )
        return listOf(FeedListError(errorItemContent))
    }

    fun mapNoUserPosts(): List<FeedItem> {
        val emptyItemContent = ItemEmpty(
            imageId = R.drawable.image_feed_empty,
            title = resourceProvider.getString(R.string.feed_empty_title_description),
            subtitle = resourceProvider.getString(R.string.profile_posts_subtitle_description),
            buttonText = resourceProvider.getString(R.string.profile_posts_create_post_button_title)
        )
        return listOf(FeedEmpty(emptyItemContent))
    }

    fun mapNoUserLikes(): List<FeedItem> {
        val emptyItemContent = ItemEmpty(
            imageId = R.drawable.image_feed_empty,
            title = resourceProvider.getString(R.string.feed_empty_title_description),
            subtitle = resourceProvider.getString(R.string.profile_likes_subtitle_description),
            buttonText = resourceProvider.getString(R.string.profile_likes_feed_button_title)
        )
        return listOf(FeedEmpty(emptyItemContent))
    }
}
