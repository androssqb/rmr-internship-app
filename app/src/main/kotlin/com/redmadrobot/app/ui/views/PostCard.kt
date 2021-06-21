package com.redmadrobot.app.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.updateLayoutParams
import coil.load
import coil.transform.RoundedCornersTransformation
import com.airbnb.epoxy.*
import com.google.android.material.card.MaterialCardView
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.ItemPostBinding
import com.redmadrobot.extensions.viewbinding.inflateViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PostCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialCardViewStyle,
) : MaterialCardView(context, attrs, defStyleAttr) {

    private companion object {
        const val IMAGE_CORNER_RADIUS = R.dimen.coil_image_corner_radius_12_dp
        const val VERTICAL_MARGIN = R.dimen.friend_card_vertical_margin_dp
    }

    private val binding: ItemPostBinding = inflateViewBinding()

    private val verticalMargin = resources.getDimensionPixelOffset(VERTICAL_MARGIN)
    private val imageCornerRadius = resources.getDimension(IMAGE_CORNER_RADIUS)

    @TextProp
    fun setPostText(postText: CharSequence?) {
        binding.postText.text = postText
    }

    @TextProp
    fun setGeo(geo: CharSequence?) {
        binding.postGeoButton.text = geo
    }

    @ModelProp
    fun loadImage(url: String?) {
        url?.let {
            binding.postPhoto.load(it) {
                placeholder(R.drawable.post_photo_background)
                transformations(RoundedCornersTransformation(imageCornerRadius))
            }
        } ?: binding.postPhoto.setImageResource(R.drawable.post_photo_background)
    }

    @TextProp
    fun setAuthorNick(authorNick: CharSequence?) {
        binding.postAuthorNick.text = authorNick
    }

    @ModelProp
    fun setLiked(liked: Boolean) {
        val likeImageId = if (liked) R.drawable.icon_post_liked else R.drawable.icon_post_not_liked
        binding.postLike.setImageResource(likeImageId)
    }

    @CallbackProp
    fun setLikeClickListener(clickListener: OnClickListener?) {
        binding.postLike.setOnClickListener(clickListener)
    }

    @AfterPropsSet
    fun setMargins() {
        updateLayoutParams<MarginLayoutParams> {
            topMargin = verticalMargin
            bottomMargin = verticalMargin
        }
    }
}
