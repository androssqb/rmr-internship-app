package com.redmadrobot.app.ui.profile.friends

import com.airbnb.epoxy.TypedEpoxyController
import com.redmadrobot.app.model.*
import com.redmadrobot.app.ui.views.footer
import com.redmadrobot.app.ui.views.friendCard
import com.redmadrobot.app.ui.views.itemEmptyOrError

class ProfileFriendsEpoxyController(
    private val onCrossClicked: (friendId: String) -> Unit,
    private val onSearchFriendsClicked: () -> Unit,
    private val onUpdateClicked: () -> Unit,
) : TypedEpoxyController<List<FriendItem>>() {

    private companion object {
        const val FOOTER_KEY = "Footer"
    }

    override fun buildModels(friendList: List<FriendItem>) {
        friendList.forEachIndexed { index, friendItem ->
            when (friendItem) {
                is Friend -> {
                    friendCard {
                        id(friendItem.id)
                        fullName(friendItem.fullName)
                        nickname(friendItem.nickname)
                        loadAvatar(friendItem.avatarUrl)
                        actionIcon(friendItem.actionIconId)
                        actionListener { _ -> this@ProfileFriendsEpoxyController.onCrossClicked(friendItem.id) }
                    }
                }

                is FriendsFooter -> {
                    footer {
                        id(FOOTER_KEY)
                        buttonText(friendItem.footer)
                        searchListener { _ -> this@ProfileFriendsEpoxyController.onSearchFriendsClicked() }
                    }
                }

                is NoFriends -> {
                    itemEmptyOrError {
                        val emptyData = friendItem.itemEmpty
                        id(index)
                        title(emptyData.title)
                        subTitle(emptyData.subtitle)
                        buttonText(emptyData.buttonText)
                        buttonClickListener { _ -> this@ProfileFriendsEpoxyController.onSearchFriendsClicked() }
                    }
                }

                is FriendListError -> {
                    itemEmptyOrError {
                        val errorData = friendItem.itemError
                        id(index)
                        title(errorData.title)
                        subTitle(errorData.subtitle)
                        buttonText(errorData.buttonText)
                        buttonClickListener { _ -> this@ProfileFriendsEpoxyController.onUpdateClicked() }
                    }
                }
            }
        }
    }
}
