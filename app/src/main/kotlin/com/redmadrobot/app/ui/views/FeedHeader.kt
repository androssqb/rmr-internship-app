package com.redmadrobot.app.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.google.android.material.textview.MaterialTextView
import com.redmadrobot.app.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class FeedHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialTextView(context, attrs, defStyleAttr) {

    private companion object {
        const val VERTICAL_MARGIN = R.dimen.feed_header_vertical_padding_dp
    }

    private val verticalPadding = resources.getDimensionPixelOffset(VERTICAL_MARGIN)

    init {
        setTextAppearance(R.style.TextAppearance_InternshipApp_Body1_Black)
        gravity = Gravity.CENTER
        setBackgroundColor(Color.TRANSPARENT)
        setPadding(0, verticalPadding, 0, verticalPadding)
    }

    @TextProp
    fun setHeader(header: CharSequence?) {
        this.text = header
    }
}
