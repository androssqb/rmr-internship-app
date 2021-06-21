package com.redmadrobot.app.utils.converters

import com.redmadrobot.app.R
import com.redmadrobot.app.model.ItemEmpty
import com.redmadrobot.app.model.Profile
import com.redmadrobot.app.model.ProfileItem
import com.redmadrobot.app.model.ProfileListError
import com.redmadrobot.data.model.SearchResultProfile
import com.redmadrobot.domain.providers.ResourceProvider
import javax.inject.Inject

class ProfileListMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userDataConverter: UserDataConverter,
) {

    fun mapToUserList(profileList: Collection<SearchResultProfile>): List<ProfileItem> {
        return profileList.map { profile ->
            Profile(
                id = profile.id,
                fullName = userDataConverter
                    .convertToFullName(firstName = profile.firstName, lastName = profile.lastName),
                nickname = userDataConverter.convertToNickname(nickname = profile.nickname),
                avatarUrl = profile.avatarUrl.orEmpty(),
                actionIconId = if (profile.isFriend) 0 else R.drawable.icon_plus
            )
        }
    }

    fun profileListError(): List<ProfileItem> {
        val errorItemContent = ItemEmpty(
            imageId = R.drawable.image_feed_error,
            title = resourceProvider.getString(R.string.error_title_description),
            subtitle = resourceProvider.getString(R.string.error_subtitle_description),
            buttonText = resourceProvider.getString(R.string.update_button_title)
        )
        return listOf(ProfileListError(errorItemContent))
    }
}
