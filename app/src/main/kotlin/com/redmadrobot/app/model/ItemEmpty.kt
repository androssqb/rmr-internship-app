package com.redmadrobot.app.model

import androidx.annotation.DrawableRes

data class ItemEmpty(
    @DrawableRes
    val imageId: Int = 0,
    val title: String,
    val subtitle: String,
    val buttonText: String
)
