package com.redmadrobot.app.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.*
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.ItemEmptyOrErrorBinding
import com.redmadrobot.extensions.viewbinding.inflateViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class ItemEmptyOrError @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private companion object {
        const val TOP_MARGIN = R.dimen.feed_no_image_margin_160_dp
    }

    private val binding: ItemEmptyOrErrorBinding = inflateViewBinding()
    private val topMargin = resources.getDimensionPixelSize(TOP_MARGIN)

    @JvmOverloads
    @ModelProp
    fun setImage(@DrawableRes imageId: Int = 0) {
        if (imageId == 0) {
            binding.emptyOrErrorImage.isVisible = false
            binding.emptyOrErrorTitle.updateLayoutParams<MarginLayoutParams> {
                topMargin = this@ItemEmptyOrError.topMargin
            }
        } else {
            binding.emptyOrErrorImage.isVisible = true
            binding.emptyOrErrorImage.setImageResource(imageId)
            binding.emptyOrErrorTitle.updateLayoutParams<MarginLayoutParams> {
                topMargin = 0
            }
        }
    }

    @TextProp
    fun setTitle(title: CharSequence?) {
        binding.emptyOrErrorTitle.text = title
    }

    @TextProp
    fun setSubTitle(subTitle: CharSequence?) {
        binding.emptyOrErrorSubtitle.text = subTitle
    }

    @TextProp
    fun setButtonText(buttonText: CharSequence?) {
        binding.emptyOrErrorButton.text = buttonText
    }

    @CallbackProp
    fun setButtonClickListener(clickListener: OnClickListener?) {
        binding.emptyOrErrorButton.setOnClickListener(clickListener)
    }
}
