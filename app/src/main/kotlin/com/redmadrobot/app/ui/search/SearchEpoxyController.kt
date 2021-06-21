package com.redmadrobot.app.ui.search

import com.airbnb.epoxy.TypedEpoxyController
import com.redmadrobot.app.model.*
import com.redmadrobot.app.ui.views.friendCard
import com.redmadrobot.app.ui.views.itemEmptyOrError

class SearchEpoxyController(
    private val onPlusClicked: (friendId: String) -> Unit,
    private val onUpdateClicked: () -> Unit,
) : TypedEpoxyController<List<ProfileItem>>() {

    override fun buildModels(profileList: List<ProfileItem>) {
        profileList.forEachIndexed { index, profileItem ->
            when (profileItem) {
                is Profile -> {
                    friendCard {
                        id(profileItem.id)
                        fullName(profileItem.fullName)
                        nickname(profileItem.nickname)
                        loadAvatar(profileItem.avatarUrl)
                        actionIcon(profileItem.actionIconId)
                        actionListener { _ -> this@SearchEpoxyController.onPlusClicked(profileItem.id) }
                    }
                }

                is ProfileListError -> {
                    itemEmptyOrError {
                        val errorData = profileItem.itemError
                        id(index)
                        image(errorData.imageId)
                        title(errorData.title)
                        subTitle(errorData.subtitle)
                        buttonText(errorData.buttonText)
                        buttonClickListener { _ -> this@SearchEpoxyController.onUpdateClicked() }
                    }
                }
            }
        }
    }
}
