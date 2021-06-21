package com.redmadrobot.app.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.view.updateLayoutParams
import coil.clear
import coil.load
import coil.transform.RoundedCornersTransformation
import com.airbnb.epoxy.*
import com.google.android.material.card.MaterialCardView
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.ItemFriendBinding
import com.redmadrobot.extensions.viewbinding.inflateViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class FriendCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialCardViewStyle,
) : MaterialCardView(context, attrs, defStyleAttr) {

    private companion object {
        const val IMAGE_CORNER_RADIUS = R.dimen.coil_image_corner_radius_12_dp
        const val VERTICAL_MARGIN = R.dimen.friend_card_vertical_margin_dp
    }

    private val binding: ItemFriendBinding = inflateViewBinding()

    private val verticalMargin = resources.getDimensionPixelOffset(VERTICAL_MARGIN)
    private val imageCornerRadius = resources.getDimension(IMAGE_CORNER_RADIUS)

    @TextProp
    fun setFullName(fullName: CharSequence?) {
        binding.friendsUserFullName.text = fullName
    }

    @ModelProp
    fun loadAvatar(url: String) {
        if (url.isNotEmpty()) {
            binding.friendsUserAvatar.load(url) {
                placeholder(R.drawable.post_photo_background)
                transformations(RoundedCornersTransformation(imageCornerRadius))
            }
        } else {
            binding.friendsUserAvatar.clear()
            binding.friendsUserAvatar.setImageResource(R.drawable.image_profile_avatar_empty)
        }
    }

    @TextProp
    fun setNickname(nickname: CharSequence?) {
        binding.friendsUserNickname.text = nickname
    }

    @ModelProp
    fun setActionIcon(@DrawableRes imageId: Int) {
        if (imageId != 0) {
            binding.friendsUserActionButton.setImageResource(imageId)
            binding.friendsUserActionButton.isEnabled = true
        } else {
            binding.friendsUserActionButton.setImageDrawable(null)
            binding.friendsUserActionButton.isEnabled = false
        }
    }

    @CallbackProp
    fun setActionListener(clickListener: OnClickListener?) {
        binding.friendsUserActionButton.setOnClickListener(clickListener)
    }

    @AfterPropsSet
    fun setMargins() {
        updateLayoutParams<MarginLayoutParams> {
            topMargin = verticalMargin
            bottomMargin = verticalMargin
        }
    }
}
