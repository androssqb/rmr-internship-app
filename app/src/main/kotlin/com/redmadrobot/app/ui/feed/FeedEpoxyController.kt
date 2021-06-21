package com.redmadrobot.app.ui.feed

import com.airbnb.epoxy.TypedEpoxyController
import com.redmadrobot.app.model.*
import com.redmadrobot.app.ui.views.feedHeader
import com.redmadrobot.app.ui.views.itemEmptyOrError
import com.redmadrobot.app.ui.views.postCard

class FeedEpoxyController(
    private val onLikeClicked: (postId: String) -> Unit,
    private val onSearchClicked: () -> Unit,
    private val onUpdateClicked: () -> Unit,
) : TypedEpoxyController<List<FeedItem>>() {

    private companion object {
        const val HEADER_KEY = "Header"
    }

    override fun buildModels(feedList: List<FeedItem>) {
        feedList.forEachIndexed { index, feedItem ->
            when (feedItem) {
                is FeedHeader -> {
                    feedHeader {
                        id(HEADER_KEY)
                        header(feedItem.header)
                    }
                }

                is FeedPost -> {
                    postCard {
                        id(feedItem.id)
                        postText(feedItem.text)
                        loadImage(feedItem.imageUrl)
                        geo(feedItem.geo)
                        authorNick(feedItem.authorNick)
                        liked(feedItem.liked)
                        likeClickListener { _ -> this@FeedEpoxyController.onLikeClicked(feedItem.id) }
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
                        buttonClickListener { _ -> this@FeedEpoxyController.onSearchClicked() }
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
                        buttonClickListener { _ -> this@FeedEpoxyController.onUpdateClicked() }
                    }
                }
            }
        }
    }
}
