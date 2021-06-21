package com.redmadrobot.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.google.android.material.button.MaterialButton
import com.redmadrobot.app.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Footer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.borderlessButtonStyle,
) : MaterialButton(context, attrs, defStyleAttr) {

    private companion object {
        const val VERTICAL_MARGIN = R.dimen.friends_footer_vertical_margin_dp
        const val BUTTON_HEIGHT = R.dimen.button_height_dp_55
    }

    private val verticalMargin = resources.getDimensionPixelOffset(VERTICAL_MARGIN)

    init {
        height = resources.getDimensionPixelOffset(BUTTON_HEIGHT)
    }

    @TextProp
    fun setButtonText(buttonText: CharSequence?) {
        text = buttonText
    }

    @CallbackProp
    fun setSearchListener(clickListener: OnClickListener?) {
        setOnClickListener(clickListener)
    }

    @AfterPropsSet
    fun setMargins() {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = verticalMargin
            bottomMargin = verticalMargin
        }
    }
}
