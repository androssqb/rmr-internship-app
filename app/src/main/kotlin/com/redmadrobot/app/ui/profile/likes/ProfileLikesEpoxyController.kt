package com.redmadrobot.app.ui.profile.likes

import com.airbnb.epoxy.TypedEpoxyController
import com.redmadrobot.app.model.*
import com.redmadrobot.app.ui.views.itemEmptyOrError
import com.redmadrobot.app.ui.views.postCard

class ProfileLikesEpoxyController(
    private val onLikeClicked: (postId: String) -> Unit,
    private val onGoToFeedClicked: () -> Unit,
    private val onUpdateClicked: () -> Unit,
) : TypedEpoxyController<List<FeedItem>>() {

    override fun buildModels(favoriteList: List<FeedItem>) {
        favoriteList.forEachIndexed { index, feedItem ->
            when (feedItem) {
                is FeedPost -> {
                    postCard {
                        id(feedItem.id)
                        postText(feedItem.text)
                        loadImage(feedItem.imageUrl)
                        geo(feedItem.geo)
                        authorNick(feedItem.authorNick)
                        liked(feedItem.liked)
                        likeClickListener { _ -> this@ProfileLikesEpoxyController.onLikeClicked(feedItem.id) }
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
                        buttonClickListener { _ -> this@ProfileLikesEpoxyController.onGoToFeedClicked() }
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
                        buttonClickListener { _ -> this@ProfileLikesEpoxyController.onUpdateClicked() }
                    }
                }

                else -> {
                    // do noting
                }
            }
        }
    }
}
