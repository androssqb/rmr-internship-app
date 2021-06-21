package com.redmadrobot.app.ui.profile.posts

import com.airbnb.epoxy.TypedEpoxyController
import com.redmadrobot.app.model.*
import com.redmadrobot.app.ui.views.itemEmptyOrError
import com.redmadrobot.app.ui.views.postCard

class ProfilePostsEpoxyController(
    private val onCreatePostsClicked: () -> Unit,
    private val onUpdateClicked: () -> Unit,
) : TypedEpoxyController<List<FeedItem>>() {

    override fun buildModels(userPostList: List<FeedItem>) {
        userPostList.forEachIndexed { index, feedItem ->
            when (feedItem) {
                is FeedPost -> {
                    postCard {
                        id(feedItem.id)
                        postText(feedItem.text)
                        loadImage(feedItem.imageUrl)
                        geo(feedItem.geo)
                        authorNick(feedItem.authorNick)
                        liked(feedItem.liked)
                    }
                }

                is FeedEmpty -> {
                    itemEmptyOrError {
                        val emptyData = feedItem.itemEmpty
                        id(index)
                        image(emptyData.imageId)
                        title(emptyData.title)
                        subTitle(emptyData.subtitle)
                        buttonText(emptyData.buttonText)
                        buttonClickListener { _ -> this@ProfilePostsEpoxyController.onCreatePostsClicked() }
                    }
                }

                is FeedListError -> {
                    itemEmptyOrError {
                        val errorData = feedItem.itemError
                        id(index)
                        image(errorData.imageId)
                        title(errorData.title)
                        subTitle(errorData.subtitle)
                        buttonText(errorData.buttonText)
                        buttonClickListener { _ -> this@ProfilePostsEpoxyController.onUpdateClicked }
                    }
                }

                else -> {
                    // do noting
                }
            }
        }
    }
}
